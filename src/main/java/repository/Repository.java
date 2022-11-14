package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private Class<T> clz;


    public Repository(Class<T> clz) throws SQLException, ClassNotFoundException {
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
}
