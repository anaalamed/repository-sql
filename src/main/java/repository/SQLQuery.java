package repository;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static repository.FieldType.isBoxedPrimitive;

public class SQLQuery {
    private final String query;

    private SQLQuery(SQLQueryBuilder builder) {
        this.query = builder.query;
    }

    @Override
    public String toString() {
        return query;
    }

    public static class SQLQueryBuilder {
        private String query;

        public <T> SQLQueryBuilder createTable(Class<T> clz) {
            this.query = String.format("CREATE TABLE %s (", parseTableName(clz));

            try {
                List<Field> classFields = ReflectionUtils.getClassFields(clz);

                for (Field field : classFields) {
                    query += String.format("%s %s %s,", field.getName(), getFieldSQLType(field), getAnnotationsFromField(field));
                }
                query = query.substring(0, query.length() - 1) + ")";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return this;
        }

        public SQLQueryBuilder select() {
            query = "SELECT *";

            return this;
        }

        public <T> SQLQueryBuilder from(Class<T> clz) {
            query += " FROM " + parseTableName(clz);
            return this;
        }

        public SQLQueryBuilder where(List<String> conditions) {
            if (conditions.size() == 0) {
                return this;
            }

            query += " WHERE ";
            for (int i = 0; i < conditions.size(); i++) {
                query += conditions.get(i);

                if (i < conditions.size() - 1) {
                    query += " AND ";
                }
            }

            return this;
        }

        public <T> SQLQueryBuilder insertInto(T object) {
            List<Field> fields = ReflectionUtils.getClassFields(object.getClass());
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();

            for (Field field : fields) {
                keys.add(field.getName());

                try {
                    if (field.getType().isPrimitive() || isBoxedPrimitive(field.getType())) {
                        values.add(field.get(object).toString());
                    } else {
                        String jsonString = "\"" + new Gson().toJson(field.get(object)) + "\"";
                        values.add(jsonString.replace("\"\"", "\""));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String valuesStr = String.join(", ", values);
            String keysStr = String.join(", ", keys);

            query = "INSERT INTO " + parseTableName(object.getClass()) + " (" + keysStr + ") " + "VALUES (" + valuesStr + ")";

            return this;
        }

        public SQLQueryBuilder delete() {
            query = "DELETE ";
            return this;
        }

        public <T> SQLQueryBuilder dropTable(Class<T> clz) {
            query = "DROP TABLE " + parseTableName(clz);
            return this;
        }

        public SQLQuery build() {
            return new SQLQuery(this);
        }

        private static <T> String parseTableName(Class<T> clz) {
            return clz.getSimpleName().toLowerCase();
        }

        private String getFieldSQLType(Field field) {
            String fieldTypeValue = field.getType().toString().substring(field.getType().toString().lastIndexOf('.') + 1).toUpperCase();

            boolean isSQLField = Arrays.stream(FieldType.values()).anyMatch((t) -> t.name().equals(fieldTypeValue));
            String result = isSQLField ? FieldType.valueOf(fieldTypeValue).toString() : FieldType.OBJECT.toString();

            return result;
        }


        private String getAnnotationsFromField(Field field) {

            StringBuilder constraints = new StringBuilder();
            try {
                Annotation[] annotations = field.getAnnotations();

                for (Annotation annotation : annotations) {
                    Class<? extends Annotation> type = annotation.annotationType();
                    Method[] methods = type.getDeclaredMethods();
                    for (Method method : methods) {
                        Object value = method.invoke(annotation, (Object[]) null);
                        constraints.append(value).append(" ");
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return constraints.toString();
        }
    }
}
