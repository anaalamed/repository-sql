package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private Class<T> clz;


    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public <T> List<T> executeQuery(String query) {
        List<T> results = null;

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement stmt = connection.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            results = (List<T>) extractResults(resultSet);
            connection.getConnection().close();
        } catch (Exception e) {
            System.out.println(e);
        }

        return results;
    }

    private List<T> extractResults(ResultSet resultSet) {
        List<T> results = new ArrayList<>();

        try {
            while(resultSet.next()) {
                Constructor <T> constructor = (Constructor<T>) clz.getConstructor(null);
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

    public void createTable() {
        String query = new SQLQuery.SQLQueryBuilder().createTable(clz).build().toString();

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement statement = connection.getConnection().createStatement();
            statement.executeUpdate(query);
            System.out.println("Table Created");
        }
        catch (SQLException e ) {
            System.out.println("An error has occured on Table Creation");
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            System.out.println("An Mysql drivers were not found");
        }
    }
}
