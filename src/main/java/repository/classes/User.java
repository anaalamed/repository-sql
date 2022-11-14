package repository.classes;

import repository.annotations.Constraints;
import repository.annotations.PrimaryKey;

import java.util.List;

public class User {
    @PrimaryKey(primaryKey = Constraints.PRIMARY_KEY)
    private int id;
    private double weight;
    private boolean isDeveloper;
    private String name;
//    private List<Animal> animals;


    public User(int id, double weight, boolean isDeveloper, String name) {
        this.id = id;
        this.weight = weight;
        this.isDeveloper = isDeveloper;
        this.name = name;
//        this.animals = animals;
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

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
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
