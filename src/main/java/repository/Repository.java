package repository;

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


    public void deleteByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().delete().from(clz).where(conditions).build().toString();
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

    public void insertOne(T object) {
        String query = new SQLQuery.SQLQueryBuilder().insertInto(object).build().toString();

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json");
             Statement statement = connection.getConnection().createStatement()) {
            statement.execute(query);
            System.out.println("Item was successfully inserted");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

}





