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
        animals.add(new Animal(1, "a", "a"));
        animals.add(new Animal(2, "b", "b"));
        userRepository.insertOne(new User(2, 2.3, true, "ana", animals));
//        userRepository.insertOne(new User(2, 1.9, true, "ana", animals));
        animalRepository.insertOne(new Animal(3, "qq", "qqq"));

        // ------------------- insert many -----------------------------
        animalRepository.insertMany(animals);


        // ------------------- get all -----------------------------
        List<User> users = userRepository.getAll();
        List<Animal> animalsList = animalRepository.getAll();
        System.out.println(animalsList);

    }
}