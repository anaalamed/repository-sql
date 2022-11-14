package repository.classes;

public class Animal {

    private int id;
    private String name;
    private String type;

    public Animal(int id, String name, String type) {
        this.id = id;
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
