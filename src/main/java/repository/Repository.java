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

    public <T> List<T> executeQuery( ) {
        try {
            Class.forName(("com.mysql.jdbc.Driver"));
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234" );
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery( "select * from user");
            List<T> results = new ArrayList<>();

            while( rs.next()) {
                Constructor <T> constructor = (Constructor<T>) clz.getConstructor(null);
                T item = constructor.newInstance();
                Field[] declaredFields = clz.getDeclaredFields();

                for (Field field: declaredFields ) {
                    field.setAccessible(true);
                    field.set(item, rs.getObject(field.getName()));
                }
            results.add(item);
            }

            con.close();
            return results;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
