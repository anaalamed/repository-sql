package repository;

import repository.classes.Animal;
import repository.classes.User;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("================= HELLO ABADAY! =================");

        System.out.println("------------------- create table -----------------------------");
        Repository<Animal> animalRepository = new Repository<>(Animal.class);
        Repository<User> userRepository = new Repository<>(User.class);
        userRepository.createTable();
        animalRepository.createTable();

        System.out.println("\n------------------- insert one -----------------------------");
        userRepository.insertOne(new User(1, 1.9999, false, "ana"));
        userRepository.insertOne(new User(2, 3.12, true, "khaled"));
        userRepository.insertOne(new User(3, 1.9999, true, "lior"));
        userRepository.insertOne(new User(4, 1.9999, true, "delete"));

        // check for Object
        ArrayList<Animal> animalsForUser = new ArrayList<>();
        animalsForUser.add(new Animal(1, "a", "a"));
        animalsForUser.add(new Animal(2, "b", "b"));
        userRepository.insertOne(new User(5, 2.3, true, "ana", new Animal(1, "a", "a")));
//        userRepository.insertOne(new User(5, 2.3, true, "ana", animalsForUser));

        System.out.println("\n------------------- insert many -----------------------------");
        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Animal(1, "a", "a"));
        animals.add(new Animal(2, "b", "b"));
        animalRepository.insertMany(animals);

        System.out.println("\n------------------- get all -----------------------------");
        List<User> users = userRepository.getAll();
        System.out.println(users);

        List<Animal> animalsList = animalRepository.getAll();
        System.out.println(animalsList);

        System.out.println("\n------------------- get by id -----------------------------");
        User user = userRepository.getById(2);
        System.out.println(user);

        System.out.println("\n------------------- get by property -----------------------------");
        users = userRepository.getByProperty("id", 1);
        System.out.println(users);
        users = userRepository.getByProperty("name", "lior");
        System.out.println(users);

        System.out.println("\n------------------- update by property -----------------------------");
        userRepository.updateByProperty("weight", 50, "id", 2);
        userRepository.updateByProperty("isDeveloper", 1, "name", "ana");

        System.out.println("\n------------------- delete by property -----------------------------");
        userRepository.deleteByProperty("id", 4);

        System.out.println("\n------------------- truncate table -----------------------------");
        userRepository.truncateTable();

        System.out.println("\n------------------- delete table -----------------------------");
        userRepository.dropTable();
        animalRepository.dropTable();
    }
}