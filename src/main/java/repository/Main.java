package repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import repository.classes.Animal;
import repository.classes.User;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        // ------------------- get all -----------------------------
        System.out.println("Hello abadayy!");
        Repository<User> userRepository = new Repository<>(User.class);
        List<User> users = userRepository.executeQuery("select * from user");
        System.out.println(users);
        System.out.println(users);

//        // ------------------- insert One -----------------------------
//        ArrayList<Animal> animals = new ArrayList<>();
//        animals.add(new Animal(1));
//        animals.add(new Animal(2));
//        userRepository.insertOne(new User(24, 2.34, false, "aaaa", animals));

//        // ------------------- create table -----------------------------
//        Repository<Animal> repository = new Repository<>(Animal.class);
//        repository.createTable();
//
//        Field idPrimary = User.class.getDeclaredField("id");
//        Annotation[] annotations = idPrimary.getAnnotations();
//        Class<? extends Annotation> type = annotations[0].annotationType();
//        System.out.println("Values of " + type.getName());
//        for (Method method : type.getDeclaredMethods()) {
//            Object value = method.invoke(annotations[0], (Object[])null);
//            System.out.println(" " + method.getName() + ": " + value);
//        }



    }
}