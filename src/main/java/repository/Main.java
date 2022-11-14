package repository;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Hello abadayy!");
        Repository repository = new Repository(User.class);
        List users = repository.executeQuery("select * from user");
        System.out.println(users);
    }
}