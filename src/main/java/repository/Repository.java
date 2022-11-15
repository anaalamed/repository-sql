package repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Repository<T> {
    private final Class<T> clz;


    private static Logger logger = LogManager.getLogger(Repository.class.getName());




    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public void createTable() {
        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            System.out.println(query);
            statement.executeUpdate(query);
            System.out.println("Table Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getAll() {
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build().toString();
        logger.debug("query: " + query);
        return executeSelectQuery(query);
    }

    public T getById(int id) {
        List<T> results = getByProperty("id", id);
        return results.get(0);
    }

    public List<T> getByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build().toString();
        logger.debug("query: " + query);

        return executeSelectQuery(query);
    }

    public void insertOne(T object) {
            logger.info("insertOne");
            String query = new SQLQuery.SQLQueryBuilder().insertOne(object).build().toString();
            logger.debug("query: " + query);
            executeUpdateQuery(query);
    }

    public void insertMany(List<T> objects) {
        // new query for training
        logger.info("insertMany");
        String query = new SQLQuery.SQLQueryBuilder().insertMany(objects).build().toString();
        logger.debug("query: " + query);
        executeUpdateQuery(query);
    }


    public void deleteByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(clz).where(conditions).build().toString();
        logger.debug("query: " + query);
        executeUpdateQuery(query);
    }

    public void updateByProperty(String propertyNameToUpdate, Object valueToUpdate, String propertyNameCondition, Object valueCondition) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyNameCondition + " = \"" + valueCondition + "\"");

        List<String> updates = new ArrayList<>();
        updates.add(propertyNameToUpdate + " = \"" + valueToUpdate + "\"");

        String query = new SQLQuery.SQLQueryBuilder().update(clz).set(updates).where(conditions).build().toString();
        logger.debug("query: " + query);
        executeUpdateQuery(query);
    }

    public void deleteTable() {
        String query = new SQLQuery.SQLQueryBuilder().dropTable(clz).build().toString();
        logger.debug("query: " + query);
        executeUpdateQuery(query);
    }


    // --------------------------- help methods ---------------------------------
    public List<T> executeSelectQuery(String query) {
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
            Statement statement = connection.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
            System.out.printf("%d rows in set%n", results.size());
        } catch (SQLException ex) {
            logger.error("Illegal SQL operation: " + ex.getMessage());
            throw new IllegalArgumentException("You are trying to execute an illegal SQL operation: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Something went wrong: " + ex.getMessage());
            throw new RuntimeException("Something went wrong: " + ex.getMessage());
        }

        return results;
    }

    public void executeUpdateQuery(String query) {
        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            int countEffectedRows = statement.executeUpdate(query);
            logger.info("Query OK, %d row affected%n" + countEffectedRows);
        } catch (SQLException ex) {
            logger.error("Illegal SQL operation: " + ex.getMessage());
            throw new IllegalArgumentException("You are trying to execute an illegal SQL operation: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("Something went wrong: " + ex.getMessage());
            throw new RuntimeException("Something went wrong: " + ex.getMessage());
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





