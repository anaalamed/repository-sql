package repository;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        private static Logger logger = LogManager.getLogger(SQLQueryBuilder.class.getName());


        public SQLQueryBuilder select() {
            query = "SELECT *";
            return this;
        }

        public SQLQueryBuilder delete() {
            query = "DELETE ";
            return this;
        }

        public <T> SQLQueryBuilder from(Class<T> clz) {
            query += " FROM " + parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder update(Class<T> clz) {
            query = " UPDATE " + parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder dropTable(Class<T> clz) {
            query = "DROP TABLE " + parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder set(List<String> updates) {

            if (updates.size() == 0) {
                return this;
            }

            query += " SET ";
            for (int i = 0; i < updates.size(); i++) {
                query += updates.get(i);

                if (i < updates.size() - 1) {
                    query += " AND ";
                }
            }
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





        public SQLQuery build() {
            return new SQLQuery(this);
        }

        // -------------- building dynamic query inside the methods -----------------
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


        public <T> SQLQueryBuilder insertOne(T object) {
            logger.info("insertOne");
            String[] keysValuesArr = getKeysValuesOfObject(object);
            logger.debug("values: " + keysValuesArr[1]);

            query = "INSERT INTO " + parseTableName(object.getClass()) + " (" + keysValuesArr[0] + ") "
                    + "VALUES (" + keysValuesArr[1] + ")";

            return this;
        }

        public <T> SQLQueryBuilder insertMany(List<T> objects) {
            String keys = "";
            ArrayList<String> values = new ArrayList<>();

            for (T object: objects) {
                String[] keysValuesArr = getKeysValuesOfObject(object);
                keys =  ("(" + keysValuesArr[0] + ")");
                values.add("(" + keysValuesArr[1] + ")");
            }

            String valuesStr = String.join(", ", values);

            query = "INSERT INTO " + parseTableName(objects.get(0).getClass()) + keys
                    + "VALUES" + valuesStr;

            return this;
        }


        // --------------------------- help methods ---------------------------------
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
        private static <T> String[] getKeysValuesOfObject(T object) {
            List<Field> fields = ReflectionUtils.getClassFields(object.getClass());
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();

            for (Field field : fields) {
                keys.add(field.getName());

                try {
                    if (field.getType().isPrimitive() || isBoxedPrimitive(field.getType())) {
                        values.add(field.get(object).toString());
                    } else {
                        Gson gson = new Gson();
                        values.add(gson.toJson(field.get(object)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String keysStr = String.join(", ", keys);
            String valuesStr = String.join(", ", values);
            return new String[]{keysStr, valuesStr};

        }
    }
}
