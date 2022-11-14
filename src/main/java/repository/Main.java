package repository;

import repository.classes.Animal;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Hello abadayy!");
//        Repository<User> repository = new Repository(User.class);
        Repository<Animal> repository = new Repository<>(Animal.class);
        repository.createTable();
    }
}