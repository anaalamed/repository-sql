package repository.classes;

import repository.annotations.ColumnsRules;
import repository.annotations.PrimaryKey;

public class Vehicle {

    private int id;
    private String name;

    public Vehicle(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Vehicle() {
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
}
