package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Repository<T> {
    private Class<T> clz;


    public Repository(Class<T> clz) {
        this.clz = clz;
    }


    public List<T> getAll() {
        String query = new SQLQuery.SQLQueryBuilder().select().from(clz).build().toString();
        List<T> results = null;

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
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
        String query = new SQLQuery.SQLQueryBuilder().insertOne(object).build().toString();

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement statement = connection.getConnection().createStatement();
            statement.execute(query);
            System.out.println("Item was successfully inserted");
        }
        catch (ClassNotFoundException e) {
            System.out.println("An Mysql drivers were not found");
        } catch (SQLException e) {
            System.out.println("An error has occurred on insertOne");
            throw new RuntimeException(e);
        }
    }

    public void insertMany(List<T> objects) {
        // new query for training
        String query = new SQLQuery.SQLQueryBuilder().insertMany(objects).build().toString();

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement statement = connection.getConnection().createStatement();
            statement.execute(query);
            System.out.println("Items was successfully inserted");
        }
        catch (ClassNotFoundException e) {
            System.out.println("An Mysql drivers were not found");
        } catch (SQLException e) {
            System.out.println("An error has occurred on insertMany");
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement statement = connection.getConnection().createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }
        catch (ClassNotFoundException e) {
            System.out.println("An Mysql drivers were not found");
        } catch (SQLException e) {
            System.out.println("An error has occurred on Table Creation");
            throw new RuntimeException(e);
        }
    }
}
