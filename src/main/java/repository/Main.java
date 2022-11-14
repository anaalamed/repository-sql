package repository;

import repository.annotations.Constraints;
import repository.classes.Animal;
import repository.classes.User;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {
        System.out.println("Hello abadayy!");

//         ------------------- create table -----------------------------
        Repository<Animal> animalRepository = new Repository<>(Animal.class);
        Repository<User> userRepository = new Repository<>(User.class);
        userRepository.createTable();
       // animalRepository.createTable();

////         ------------------- insert One -----------------------------
//
//        userRepository.insertOne(new User(1, 1.9999, false, "ana"));
//        userRepository.insertOne(new User(2, 1.9999, true, "lior"));
//
////         ------------------- get all -----------------------------
//        List<User> users = userRepository.getAll();
//        System.out.println(users);
//
////         ------------------- get by id -----------------------------
//        User user = userRepository.getById(2);
//        System.out.println(user);
//
////         ------------------- get by property -----------------------------
//        users = userRepository.getByProperty("id", 1);
//        System.out.println(users);
//        users = userRepository.getByProperty("name", "lior");
//        System.out.println(users);
//    }

    }
}

