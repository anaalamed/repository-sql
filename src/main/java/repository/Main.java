package repository;

import repository.classes.Animal;
import repository.classes.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Hello abadayy!");

//         ------------------- create table -----------------------------
        Repository<Animal> animalRepository = new Repository<>(Animal.class);
        Repository<User> userRepository = new Repository<>(User.class);
        userRepository.createTable();
        animalRepository.createTable();

//         ------------------- insert One -----------------------------
//        List<Animal> animals = new ArrayList<>();
//        animals.add(new Animal(3));
//        animals.add(new Animal(5));

        userRepository.insertOne(new User(1, 1.9999, false, "ana"));
        userRepository.insertOne(new User(2, 3.12, true, "khaled"));
        userRepository.insertOne(new User(3, 1.9999, true, "lior"));

//         ------------------- get all -----------------------------
        List<User> users = userRepository.getAll();
        System.out.println(users);

//         ------------------- get by id -----------------------------
        User user = userRepository.getById(2);
        System.out.println(user);

//         ------------------- get by property -----------------------------
        users = userRepository.getByProperty("id", 1);
        System.out.println(users);
        users = userRepository.getByProperty("name", "lior");
        System.out.println(users);

//         ------------------- delete by property -----------------------------
        userRepository.deleteByProperty("id", 1);

//         ------------------- delete table -----------------------------

    }
}