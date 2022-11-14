package repository;


import java.util.ArrayList;
import repository.classes.Animal;
import repository.classes.User;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {
        System.out.println("Hello abadayy!");

        // ------------------- get all -----------------------------
        Repository userRepository = new Repository(User.class);
        List users = userRepository.executeQuery("select * from user");
        System.out.println(users);


        // ------------------- insert One -----------------------------
        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Animal(1));
        animals.add(new Animal(2));
        userRepository.insertOne(new User(24, 2.34, false, "aaaa", animals));




        // ------------------- create table -----------------------------
        Repository<Animal> repository = new Repository<>(Animal.class);
        repository.createTable();

    }
}