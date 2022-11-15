package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.utils.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


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
        private final static Logger logger = LogManager.getLogger(SQLQueryBuilder.class.getName());


        public SQLQueryBuilder select() {
            logger.info("in SQLQueryBuilder.select()");

            query = "SELECT *";
            return this;
        }

        public SQLQueryBuilder delete() {
            logger.info("in SQLQueryBuilder.delete()");

            query = "DELETE ";
            return this;
        }

        public <T> SQLQueryBuilder from(Class<T> clz) {
            logger.info("in SQLQueryBuilder.from()");

            query += " FROM " + ReflectionUtils.parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder update(Class<T> clz) {
            logger.info("in SQLQueryBuilder.update()");

            query = " UPDATE " + ReflectionUtils.parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder dropTable(Class<T> clz) {
            logger.info("in SQLQueryBuilder.dropTable()");

            query = "DROP TABLE " + ReflectionUtils.parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder truncateTable(Class<T> clz) {
            logger.info("in SQLQueryBuilder.truncateTable()");

            query = "TRUNCATE TABLE " + ReflectionUtils.parseTableName(clz);
            return this;
        }

        public <T> SQLQueryBuilder set(List<String> updates) {
            logger.info("in SQLQueryBuilder.set()");

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
            logger.info("in SQLQueryBuilder.where()");

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

        // -------------- building dynamic query inside the methods -----------------
        public <T> SQLQueryBuilder createTable(Class<T> clz) {
            logger.info("in SQLQueryBuilder.createTable()");

            this.query = String.format("CREATE TABLE %s (", ReflectionUtils.parseTableName(clz));

            try {
                List<Field> classFields = ReflectionUtils.getClassFields(clz);

                for (Field field : classFields) {
                    query += String.format("%s %s %s,", field.getName(),
                            ReflectionUtils.getFieldSQLType(field), getAnnotationsFromField(field));
                }
                query = query.substring(0, query.length() - 1) + ")";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return this;
        }

        public <T> SQLQueryBuilder insertOne(T object) {
            logger.info("in SQLQueryBuilder.insertOne()");

            String[] keysValuesArr = ReflectionUtils.getKeysValuesOfObject(object);

            query = "INSERT INTO " + ReflectionUtils.parseTableName(object.getClass()) + " (" + keysValuesArr[0] + ") "
                    + "VALUES (" + keysValuesArr[1] + ")";

            return this;
        }

        public <T> SQLQueryBuilder insertMany(List<T> objects) {
            logger.info("in SQLQueryBuilder.insertMany()");

            String keys = "";
            ArrayList<String> values = new ArrayList<>();

            for (T object: objects) {
                String[] keysValuesArr = ReflectionUtils.getKeysValuesOfObject(object);
                keys =  ("(" + keysValuesArr[0] + ")");
                values.add("(" + keysValuesArr[1] + ")");
            }

            String valuesStr = String.join(", ", values);

            query = "INSERT INTO " + ReflectionUtils.parseTableName(objects.get(0).getClass()) + keys
                    + "VALUES" + valuesStr;

            return this;
        }

        public String build() {
            return new SQLQuery(this).query;
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
