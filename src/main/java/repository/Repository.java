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


    public List<T> getAll() {
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build().toString();
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json")) {
            Statement stmt = connection.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
        } catch (Exception e) {
            System.out.println(e);
        }

        return results;
    }

    public T getById(int id) {
        List<String> conditions = new ArrayList<>();
        conditions.add("id = " + id);

        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build().toString();
        T result = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json")) {
            Statement stmt = connection.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            result = (T) extractResults(resultSet).get(0);
        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }

    public List<T> getByProperty(String propertyName, Object value) {
        List<String> conditions = new ArrayList<>();
        conditions.add(propertyName + " = \"" + value + "\"");

        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).where(conditions).build().toString();
        List<T> results = null;

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json")) {
            Statement stmt = connection.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
        } catch (Exception e) {
            System.out.println(e);
        }

        return results;
    }

    private List<T> extractResults(ResultSet resultSet) {
        List<T> results = new ArrayList<>();

        try {
            while(resultSet.next()) {
                Constructor<T> constructor = (Constructor<T>) clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field: declaredFields) {
                    field.setAccessible(true);
                    field.set(item, resultSet.getObject(field.getName()));
                }

                results.add(item);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return results;
    }

    public void insertOne(T object) {
        String query = new SQLQuery.SQLQueryBuilder().insertInto(object).build().toString();

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json")) {
            Statement statement = connection.getConnection().createStatement();
            statement.execute(query);
            System.out.println("Item was successfully inserted");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();

        try (SQLConnection connection = SQLConnection.createSQLConnection("connectionData.json")) {
            Statement statement = connection.getConnection().createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Constraints, String> getAnnotationsFromField(Field field) {
        Map<Constraints, String> constraints = new HashMap<>();
        return constraints;
    }
}