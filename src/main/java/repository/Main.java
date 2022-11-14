package repository;

import repository.annotations.ColumnsRules;
import repository.annotations.PrimaryKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, NoSuchFieldException {
        System.out.println("Hello abadayy!");
        Repository<User> repository = new Repository<>(User.class);
        List<User> users = repository.executeQuery("select * from user");
        System.out.println(users);

        Field idPrimary = User.class.getDeclaredField("id");
        Annotation annotations = idPrimary.getAnnotation(PrimaryKey.class);
        System.out.println(annotations.);

    }
}