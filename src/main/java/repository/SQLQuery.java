package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.annotations.Constraints;
import repository.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
            query = "DROP TABLE IF EXISTS " + ReflectionUtils.parseTableName(clz);
            logger.info("in SQLQueryBuilder.dropTable()");

            return this;
        }

        public <T> SQLQueryBuilder truncateTable(Class<T> clz) {
            logger.info("in SQLQueryBuilder.truncateTable()");

            query = "TRUNCATE TABLE " + ReflectionUtils.parseTableName(clz);
            return this;
        }

        public SQLQueryBuilder set(List<String> updates) {
            logger.info("in SQLQueryBuilder.set()");

            if (updates.size() == 0) {
                return this;
            }
            StringBuilder setQuery = new StringBuilder(" SET ");

            for (int i = 0; i < updates.size(); i++) {
                setQuery.append(updates.get(i));

                if (i < updates.size() - 1) {
                    setQuery.append(" AND ");
                }
            }
            query += setQuery.toString();
            return this;
        }

        public SQLQueryBuilder where(List<String> conditions) {
            logger.info("in SQLQueryBuilder.where()");

            if (conditions.size() == 0) {
                return this;
            }
            StringBuilder WhereQuery = new StringBuilder(" WHERE ");

            for (int i = 0; i < conditions.size(); i++) {
                WhereQuery.append(conditions.get(i));

                if (i < conditions.size() - 1) {
                    WhereQuery.append(" AND ");
                }
            }
            query += WhereQuery.toString();
            return this;
        }



        // -------------- building dynamic query inside the methods -----------------
        public <T> SQLQueryBuilder createTableIfNotExists(Class<T> clz) {
            Map<Constraints, Integer> annotationsCountByType = new HashMap<>();
            StringBuilder createTableQuery = new StringBuilder(String.format("CREATE TABLE IF NOT EXISTS  %s (", ReflectionUtils.parseTableName(clz)));


            try {
                List<Field> classFields = ReflectionUtils.getClassFields(clz);

                for (Field field : classFields) {
                    createTableQuery.append(String.format("%s %s %s,", field.getName(), ReflectionUtils.getFieldSQLType(field), ReflectionUtils.getAnnotationsFromField(field, annotationsCountByType)));
                }
                System.out.println(annotationsCountByType);
                query = createTableQuery.toString();
                query = query.substring(0, query.length() - 1) + ")";

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return this;
        }


        public <T> SQLQueryBuilder insertOne(T object) {

            logger.info("insertOne");
            String[] keysValuesArr = ReflectionUtils.getKeysValuesOfObject(object);
            logger.debug("values: " + keysValuesArr[1]);
            query = "INSERT INTO " + ReflectionUtils.parseTableName(object.getClass()) + " (" + keysValuesArr[0] + ") " + "VALUES (" + keysValuesArr[1] + ")";
            query = "INSERT INTO " + ReflectionUtils.parseTableName(object.getClass()) + " (" + keysValuesArr[0] + ") " + "VALUES (" + keysValuesArr[1] + ")";

            logger.info("in SQLQueryBuilder.insertOne()");

            List<T> wrappingList = new ArrayList<>();
            wrappingList.add(object);


            return insertMany(wrappingList);
        }

        public <T> SQLQueryBuilder insertMany(List<T> objects) {
            logger.info("in SQLQueryBuilder.insertMany()");

            String keys = "";
            ArrayList<String> values = new ArrayList<>();

            for (T object : objects) {
                String[] keysValuesArr = ReflectionUtils.getKeysValuesOfObject(object);
                keys = ("(" + keysValuesArr[0] + ")");
                values.add("(" + keysValuesArr[1] + ")");
            }

            String valuesStr = String.join(", ", values);

            query = "INSERT INTO " + ReflectionUtils.parseTableName(objects.get(0).getClass()) + keys + "VALUES" + valuesStr;

            return this;
        }


        public String build() {
            return new SQLQuery(this).query;
        }


    }
}



