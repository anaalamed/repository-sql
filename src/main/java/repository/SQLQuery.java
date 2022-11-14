package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Map<String, String> classFields = getClassFields(clz);

                for (Map.Entry<String, String> entry : classFields.entrySet()) {
                    query += String.format("%s %s,", entry.getKey(), entry.getValue());
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

        private static <T> Map<String, String> getClassFields(Class<T> clz) {
            Map<String, String> classFields = new HashMap<>();

            try {
                Constructor<T> constructor = (Constructor<T>) clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    String fieldTypeValue = field.getType().toString()
                            .substring(field.getType().toString().lastIndexOf('.') + 1).toUpperCase();
                    classFields.put(field.getName(), FieldType.valueOf(fieldTypeValue).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return classFields;
        }
    }
}
