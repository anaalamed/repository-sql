package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLQuery<T> {
    private String query;

    private SQLQuery(SQLQueryBuilder builder) {
        this.query = builder.query;
    }

    @Override
    public String toString() {
        return query;
    }

    public static class SQLQueryBuilder<T> {
        private String query;


        public <T> SQLQueryBuilder createTable(Class<T> clz) {
            this.query = String.format("CREATE TABLE %s (", parseTableName(clz));

            try {
                List<Field> classFields = getClassFields(clz);

                for (Field field : classFields) {
                    query += String.format("%s %s,", field.getName(), getFieldSQLType(field));
                }

                query = query.substring(0, query.length() - 1) + ")";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return this;
        }

        public SQLQueryBuilder select() {
            query += "SELECT *";

            return this;
        }

        public <T> SQLQueryBuilder from(Class<T> clz) {
            query += " FROM " + parseTableName(clz);
            return this;
        }

        public SQLQueryBuilder where(List<Boolean> conditions) {
            query += " WHERE ";
            for (int i = 0; i < conditions.size(); i++) {
                query += conditions.get(i).toString();

                if (i < conditions.size() - 1) {
                    query += " AND ";
                }
            }

            return this;
        }

        public SQLQuery build() {
            return new SQLQuery(this);
        }

        private static <T> String parseTableName(Class<T> clz) {
            return clz.getSimpleName().toLowerCase();
        }

        private static <T> List<Field> getClassFields(Class<T> clz) {
            List<Field> classFields = new ArrayList<>();

            try {
                Constructor<T> constructor = (Constructor<T>) clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    classFields.add(field);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return classFields;
        }

        private String getFieldSQLType(Field field) {
            String fieldTypeValue = field.getType().toString()
                    .substring(field.getType().toString().lastIndexOf('.') + 1).toUpperCase();

            return FieldType.valueOf(fieldTypeValue).toString();
        }
    }
}
