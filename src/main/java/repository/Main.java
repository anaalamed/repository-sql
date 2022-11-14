package repository;

import java.util.ArrayList;

import repository.classes.Animal;
import repository.classes.User;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException {
        // ------------------- get all -----------------------------
        System.out.println("Hello abadayy!");
        Repository<User> userRepository = new Repository<>(User.class);
        List<User> users = userRepository.executeQuery("select * from user");
        System.out.println(users);
        System.out.println(users);

        // ------------------- insert One -----------------------------
        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Animal(1));
        animals.add(new Animal(2));
        userRepository.insertOne(new User(24, 2.34, false, "aaaa", animals));

        // ------------------- create table -----------------------------
        Repository<Animal> repository = new Repository<>(Animal.class);
        repository.createTable();

//        Field idPrimary = User.class.getDeclaredField("id");
//        Annotation annotations = idPrimary.getAnnotation(PrimaryKey.class);
//        System.out.println(annotations.);

    }
}