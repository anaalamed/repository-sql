package repository;

import repository.classExamples.Animal;
import repository.classExamples.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello abadayy!");

        // ------------------- User -----------------------------
        Repository<User> userRepository = new Repository<>(User.class);
        List<User> users = userRepository.getItems();
        System.out.println(users);

        // ------------------- Animal -----------------------------
        Repository<Animal> animalRepository = new Repository<>(Animal.class);
        List<User> animals = animalRepository.getItems();
        System.out.println(animals);
    }
}