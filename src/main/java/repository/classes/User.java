package repository.classes;

import repository.annotations.*;

import java.util.List;

public class User {
    @PrimaryKey
    @Unique
    @Auto_Increment
    private int id;
    private double weight;
    private boolean isDeveloper;
    @NotNull
    private String name;
    private List<Animal> animals;
    private Animal favouriteAnimal;

    public User(int id, double weight, boolean isDeveloper, String name) {
        this.id = id;
        this.weight = weight;
        this.isDeveloper = isDeveloper;
        this.name = name;
    }

    public User(int id, double weight, boolean isDeveloper, String name, List<Animal> animals) {
        this.id = id;
        this.weight = weight;
        this.isDeveloper = isDeveloper;
        this.name = name;
        this.animals = animals;
    }

    public User(int id, double weight, boolean isDeveloper, String name, Animal animal) {
        this.id = id;
        this.weight = weight;
        this.isDeveloper = isDeveloper;
        this.name = name;
        this.favouriteAnimal = animal;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }


    public boolean isDeveloper() {
        return isDeveloper;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    public Animal getFavouriteAnimal() {
        return favouriteAnimal;
    }

    public void setFavouriteAnimal(Animal favouriteAnimal) {
        this.favouriteAnimal = favouriteAnimal;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", weight=" + weight +
                ", isDeveloper=" + isDeveloper +
                ", name='" + name + '\'' +
                '}';
    }
}
