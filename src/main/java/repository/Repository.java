package repository;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.utils.ReflectionUtils;
import repository.utils.SQLConnection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static repository.utils.ReflectionUtils.isComplexObject;

public class Repository<T> {
    private final Class<T> clz;
    private final String CONFIGURATION_FILENAME = "connectionData.json";
    private final static Logger logger = LogManager.getLogger(Repository.class.getName());

    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public void createTable() {
        logger.info("in createTable()");

        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build();
        logger.debug("Executing query: " + query);

        executeUpdateQuery(query);
    }

    public List<T> getAll() {
        logger.info("in getAll()");

        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build();
        logger.debug("Executing query: " + query);
        return executeSelectQuery(query);
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
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build();
        logger.debug("Executing query: " + query);

        return executeSelectQuery(query);
    }

    public T insertOne(T object) {
        logger.info("in insertOne()");
        String query = new SQLQuery.SQLQueryBuilder().insertOne(object).build();
        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);

        // get inserted entity
//        List<T> entities = getAddedEntity(object);
//        if ( entities == null || entities.size() == 0) {
//            throw new NullPointerException("Entity wasn't added");
//        }
//        T entityAdded = entities.get(0);                               // suppose there are no the same items
//        logger.debug("Inserted entity: " + entityAdded);
        return getAddedEntity(object);
    }

    public List<T> insertMany(List<T> objects) {
        logger.info("in insertMany()");
        String query = new SQLQuery.SQLQueryBuilder().insertMany(objects).build();
        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);

        // get inserted entities
        List<T> insertedEntities = new ArrayList<>();
        for (T object: objects) {
            insertedEntities.add(getAddedEntity(object));
//            List<T> entities = getAddedEntity(object);
//            if ( entities == null || entities.size() == 0) {
//                throw new NullPointerException("One or more entities weren't added");
//            }
//            insertedEntities.add(entities.get(0));                        // suppose there are no the same items
        }
        logger.debug("Inserted entities: " + insertedEntities);
        return insertedEntities;
    }

    public void deleteByProperty(String propertyName, Object value) {
        logger.info("in deleteByProperty()");

        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(clz).where(conditions).build();
        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);
    }

    public void updateByProperty(String propertyToUpdate, Object valueToUpdate,
                                 String conditionProperty, Object conditionValue) {
        logger.info("in updateByProperty()");

        List<String> conditions = new ArrayList<>();
        conditions.add(conditionProperty + " = \"" + conditionValue + "\"");

        List<String> updates = new ArrayList<>();
        updates.add(propertyToUpdate + " = \"" + valueToUpdate + "\"");

        String query = new SQLQuery.SQLQueryBuilder().update(clz).set(updates).where(conditions).build();

        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);
    }

    public void dropTable() {
        logger.info("in dropTable()");
        String query = new SQLQuery.SQLQueryBuilder().dropTable(clz).build();
        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);
    }

    public void truncateTable() {
        logger.info("in truncateTable()");

        String query = new SQLQuery.SQLQueryBuilder().truncateTable(clz).build();
        logger.debug("Executing query: " + query);
        executeUpdateQuery(query);
    }



    // -------------------- help methods ------------------------------
    private List<T> executeSelectQuery(String query) {
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection(this.CONFIGURATION_FILENAME);
            Statement statement = connection.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
            logger.info(String.format("%d rows in set", results.size()));
        } catch (SQLException ex) {
            logger.error("Illegal SQL operation: " + ex.getMessage());
            throw new IllegalArgumentException("You are trying to execute an illegal SQL operation: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Something went wrong: " + ex.getMessage());
            throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
        }

        return results;
    }

    private void executeUpdateQuery(String query) {
        try (SQLConnection connection = SQLConnection.createSQLConnection(this.CONFIGURATION_FILENAME);
             Statement statement = connection.getConnection().createStatement()) {
            int countEffectedRows = statement.executeUpdate(query);
            logger.info(String.format("Query OK, %d row affected", countEffectedRows));
        } catch (SQLException ex) {
            logger.error("Illegal SQL operation: " + ex.getMessage());
            throw new IllegalArgumentException("You are trying to execute an illegal SQL operation: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            logger.error("Something went wrong: " + ex.getMessage());
            throw new RuntimeException("Something went wrong: " + ex.getMessage(), ex);
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
                    Object object = resultSet.getObject(field.getName());
                    if (isComplexObject(field.getType())) {
                        object = new Gson().fromJson((String) object, field.getType());
                    }
                    field.set(item, object);
                }

                results.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return results;
    }

    private T getAddedEntity(T object) {
        logger.info("in getAddedEntity()");
        Map<String,String> mapKeysValues = ReflectionUtils.getMapKeysValuesOfObject(object);
        List<String> conditions = new ArrayList<>();
        for (String key: mapKeysValues.keySet() ) {
            conditions.add(key + " = " + mapKeysValues.get(key) );
        }

        String getQuery = new SQLQuery.SQLQueryBuilder().select().from(object.getClass()).where(conditions).build();
        logger.debug(getQuery);

        List<T> entities = executeSelectQuery(getQuery);
        if ( entities == null || entities.size() == 0) {
            logger.error("Entity wasn't added");
            throw new NullPointerException("Entity wasn't added");
        }
        T entityAdded = entities.get(0);                               // suppose there are no the same items
        logger.debug("Inserted entity: " + entityAdded);
        return entityAdded;
    }
}





