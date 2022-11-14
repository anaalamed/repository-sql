package repository;

import repository.annotations.Constraints;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class Repository<T> {
    private final Class<T> clz;


    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public void createTable() {
        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate(query);
            System.out.println("Table Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<T> getAll() {
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build().toString();
        return select(query);
    }

    public T getById(int id) {
        List<T> results = getByProperty("id", id);
        return results.get(0);
    }

    public List<T> getByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build().toString();

        return select(query);
    }

    public void insertOne(T object) {
        String query = new SQLQuery.SQLQueryBuilder().insertOne(object).build().toString();
        update(query);
    }

    public void insertMany(List<T> objects) {
        // new query for training
        String query = new SQLQuery.SQLQueryBuilder().insertMany(objects).build().toString();
        update(query);
    }

    public void deleteByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(clz).where(conditions).build().toString();
        update(query);
    }

    public void updateByProperty(String propertyNameToUpdate, Object valueToUpdate, String propertyNameCondition, Object valueCondition) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyNameCondition + " = \"" + valueCondition + "\"");

        List<String> updates = new ArrayList<>();
        updates.add(propertyNameToUpdate + " = \"" + valueToUpdate + "\"");

        String query = new SQLQuery.SQLQueryBuilder().update(clz).set(updates).where(conditions).build().toString();
        update(query);
    }

    public void deleteTable() {
        String query = new SQLQuery.SQLQueryBuilder().dropTable(clz).build().toString();
        update(query);
    }

    public List<T> select(String query) {
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
            Statement statement = connection.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
            System.out.println(String.format("%d rows in set", results.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public void update(String query) {
        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
            Statement statement = connection.getConnection().createStatement()) {
            int countEffectedRows = statement.executeUpdate(query);
            System.out.println(String.format("Query OK, %d row affected", countEffectedRows));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<T> extractResults(ResultSet resultSet) {
        List<T> results = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Constructor<T> constructor = (Constructor<T>) clz.getConstructor();
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

    private Map<Constraints, String> getAnnotationsFromField(Field field) {
        Map<Constraints, String> constraints = new HashMap<>();
        return constraints;
    }
}

