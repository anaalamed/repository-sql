package repository;


import repository.classExamples.Animal;
import repository.classExamples.User;
import repository.classExamples.Vehicle;


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {
        System.out.println("Hello abadayy!");

        Repository repository = new Repository(User.class);
        List users = repository.executeQuery("select * from user");

        System.out.println(users);


        ArrayList<Animal> animals = new ArrayList<>();
        animals.add(new Animal(1, "bbb", 2.0));
        animals.add(new Animal(2, "ccc", 2.0));

        repository.insertOne(new User(23, 2.34, false, "aaaa", animals));

//        // ------------------- Animal -----------------------------
//        Repository<Animal> animalRepository = new Repository<>(Animal.class);
//        List<User> animals = animalRepository.getItems();
//        System.out.println(animals);



        // ------------------- Vehicle -----------------------------
//        Repository<Vehicle> vehicleRepository = new Repository<>(Vehicle.class);
//        vehicleRepository.createTable();
    }
}