package repository.classExamples;

public class Animal {
    private int id;
    private String animalName;
    private double weight;

    public Animal(int id, String name, double weight) {
        this.id = id;
        this.animalName = name;
        this.weight = weight;
    }

    public Animal() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return animalName;
    }

    public void setName(String name) {
        this.animalName = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "\nAnimal{" +
                "id=" + id +
                ", name='" + animalName + '\'' +
                ", weight=" + weight +
                '}';
    }
}
