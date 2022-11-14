package repository;

import repository.classExamples.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class Repository<T> {
    private Class<T> clz;


    public Repository(Class<T> clz) throws ClassNotFoundException {
        this.clz = clz;
    }


    public <T> List<T> executeQuery(String query) {
        List<T> results = null;

        try {
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");

            Statement stmt = connection.getConnection().createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            results = (List<T>) extractResults(resultSet);

//            connection.getConnection().close();
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



    public <T> void insertOne(T obj) {
        try {
            // the mysql insert statement
            SQLConnection connection = SQLConnection.getInstance("connectionData.json");
            Statement st = connection.getConnection().createStatement();

            // create keysStr and valuesStr for query
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> keys = new ArrayList<>();

            for (Field field: obj.getClass().getDeclaredFields() ) {
                field.setAccessible(true);

                if (field.get(obj).getClass().getSimpleName().equals("Integer") ||
                    field.get(obj).getClass().getSimpleName().equals("Double") ||
                    field.get(obj).getClass().getSimpleName().equals("Float") ||
                    field.get(obj).getClass().getSimpleName().equals("Boolean")) {
                    values.add(field.get(obj).toString());
                    keys.add(field.getName());
                } else {
                    values.add('"' + field.get(obj).toString()+ '"');
                    keys.add(field.getName());
                }
            }

            String valuesStr = String.join(", ", values);
            String keysStr = String.join(", ", keys);

            String query = "INSERT INTO "+obj.getClass().getSimpleName()+" ("+keysStr+") "
                    +"VALUES ("+valuesStr+")";

            System.out.println(query);
            st.execute(query);
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
}
