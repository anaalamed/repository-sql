package repository;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlConnection {
    private Connection connection;
    private Map<String, String> queries;

    public SqlConnection() {
        this.connection = connection();
        this.queries = generateQueriesMap();
    }

    private Connection connection() {
        try {
            Class.forName(("com.mysql.jdbc.Driver"));
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234" );
        } catch (Exception e) {
            throw new RuntimeException("connection failed");
        }
    }

    private Map<String, String> generateQueriesMap() {
        Map<String, String> map = new HashMap<>();
        map.put("getAll", "select * from ");
        return map;
    }


    public <T> List<T> getItems( Class clz) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(queries.get("getAll")+clz.getSimpleName().toLowerCase());

            List<T> results = new ArrayList<>();
            while( rs.next()) {
                Constructor<T> constructor = (Constructor<T>) clz.getConstructor(null);
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
}
