package repository;


import repository.classExamples.Animal;
import repository.classExamples.User;
import repository.classExamples.Vehicle;


import java.util.List;

public class Main {
    public static void main(String[] args) throws  ClassNotFoundException {
        System.out.println("Hello abadayy!");

        Repository repository = new Repository(User.class);
        List users = repository.executeQuery("select * from user");

        System.out.println(users);


//        repository.insertOne(new User(10, "aaaa"));

//        // ------------------- Animal -----------------------------
//        Repository<Animal> animalRepository = new Repository<>(Animal.class);
//        List<User> animals = animalRepository.getItems();
//        System.out.println(animals);



        // ------------------- Vehicle -----------------------------
//        Repository<Vehicle> vehicleRepository = new Repository<>(Vehicle.class);
//        vehicleRepository.createTable();
    }
}