package repository;

import repository.annotations.ColumnsRules;
import repository.annotations.PrimaryKey;

public class User {
    @PrimaryKey(primaryKey = ColumnsRules.PRIMARY_KEY)
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "\nUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
