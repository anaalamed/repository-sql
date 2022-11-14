package repository;

import repository.classExamples.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SqlConnection {
    private Connection connection;
    private Map<String, String> queries;

    public SqlConnection(String tableName) {
        this.connection = connection(tableName);
        this.queries = generateQueriesMap();
    }

    private Connection connection(String tableName) {
        try {
            Class.forName(("com.mysql.jdbc.Driver"));
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/"+tableName, "root", "1234" );
        } catch (Exception e) {
            throw new RuntimeException("connection failed");
        }
    }

    private Map<String, String> generateQueriesMap() {
        Map<String, String> map = new HashMap<>();
        map.put("getAll", "select * from ");
        return map;
    }


    public <T> List<T> getItems(Class clz) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queries.get("getAll")+clz.getSimpleName().toLowerCase());

            List<T> results = new ArrayList<>();
            while( rs.next()) {
                Constructor <T> constructor = clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field: declaredFields ) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
                results.add(item);
            }

//            connection.close();       // when close???
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public <T, K, V> void insertOne(T obj) {
        try {
            // the mysql insert statement
            String query = " insert into user (name, id)"
                    + " values (?, ?)";

            Statement st = connection.createStatement();
            Class<?> clz = obj.getClass();

            Map< String, Object> map = new HashMap<>();
            Field[] declaredFields = clz.getDeclaredFields();

            // create map<Key, Value> of given object 
            for (Field field: declaredFields ) {
                field.setAccessible(true);
                System.out.println(field.getGenericType());
                map.put(field.getName(), field.get(obj));
            }


            String keys = String.join(",", map.keySet());
            System.out.println(keys);
            String values = map.values().stream().map(Object::toString).collect(Collectors.joining(","));
            System.out.println(map.values() + "-" + values);

            st.executeUpdate("INSERT INTO "+clz.getSimpleName()+" ("+keys+") "
                    +"VALUES ('aaa', 11)");

//            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }


}
