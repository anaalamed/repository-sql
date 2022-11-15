package repository.classes;

import repository.annotations.Auto_Increment;

public class Animal {

    private int id;
    private String name;
    private String type;

    public Animal(int id, String name, String type) {
        this(name, type);
        this.id = id;

    }

    public Animal(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Animal() {
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
