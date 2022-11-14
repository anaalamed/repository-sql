package repository;


import java.util.ArrayList;
import repository.classes.Animal;
import repository.classes.User;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {
        System.out.println("Hello abadayy!");

        // ------------------- create table -----------------------------
        Repository<Animal> animalRepository = new Repository<>(Animal.class);
        Repository<User> userRepository = new Repository(User.class);
        userRepository.createTable();
        animalRepository.createTable();

        // ------------------- insert One -----------------------------
        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Animal(1));
        animals.add(new Animal(2));
        userRepository.insertOne(new User(25, 1.9999, false, "ana", animals));

        // ------------------- get all -----------------------------
        List<User> users = userRepository.getAll();
        System.out.println(users);


    }
}