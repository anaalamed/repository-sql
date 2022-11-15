package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Repository<T> {
    private final Class<T> clz;
    private static Logger logger = LogManager.getLogger(Repository.class.getName());

    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public void createTable() {
        logger.info("in createTable()");

        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();
        logger.debug("Executing query: " + query);

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Table Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getAll() {
        logger.info("in getAll()");

        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build().toString();
        logger.debug("Executing query: " + query);
        return select(query);
    }

    public T getById(int id) {
        logger.info("in getById()");

        List<T> results = getByProperty("id", id);
        return results.get(0);
    }

    public List<T> getByProperty(String propertyName, Object value) {
        logger.info("in getByProperty()");

        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build().toString();
        logger.debug("Executing query: " + query);

        return select(query);
    }

    public void insertOne(T object) {
        logger.info("in insertOne()");

        String query = new SQLQuery.SQLQueryBuilder().insertOne(object).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }

    public void insertMany(List<T> objects) {
        logger.info("in insertMany()");

        String query = new SQLQuery.SQLQueryBuilder().insertMany(objects).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }


    public void deleteByProperty(String propertyName, Object value) {
        logger.info("in deleteByProperty()");

        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(clz).where(conditions).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }

    public void updateByProperty(String propertyToUpdate, Object valueToUpdate,
                                 String conditionProperty, Object conditionValue) {
        logger.info("in updateByProperty()");

        List<String> conditions = new ArrayList<>();
        conditions.add(conditionProperty + " = \"" + conditionValue + "\"");

        List<String> updates = new ArrayList<>();
        updates.add(propertyToUpdate + " = \"" + valueToUpdate + "\"");

        String query = new SQLQuery.SQLQueryBuilder().update(clz).set(updates).where(conditions).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }

    public void dropTable() {
        logger.info("in dropTable()");

        String query = new SQLQuery.SQLQueryBuilder().dropTable(clz).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }

    public void truncateTable() {
        logger.info("in truncateTable()");

        String query = new SQLQuery.SQLQueryBuilder().truncateTable(clz).build().toString();
        logger.debug("Executing query: " + query);
        update(query);
    }

    public List<T> select(String query) {
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
            System.out.printf("%d rows in set%n", results.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void update(String query) {
        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            int countEffectedRows = statement.executeUpdate(query);
            System.out.printf("Query OK, %d row affected%n", countEffectedRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<T> extractResults(ResultSet resultSet) {
        List<T> results = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Constructor<T> constructor = clz.getConstructor();
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field : declaredFields) {
                    field.setAccessible(true);
                    field.set(item, resultSet.getObject(field.getName()));
                }

                results.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;
    }

}





