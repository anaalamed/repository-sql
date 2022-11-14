package repository;

import repository.classExamples.User;

import java.util.List;

public class Repository<T> {
    private Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public <T> List<T> getItems() {
        SqlConnection mySqlConnection = new SqlConnection("test");
        return mySqlConnection.getItems(clz);
    }

    public <T> void insertOne(User user) {
        SqlConnection mySqlConnection = new SqlConnection("test");
        mySqlConnection.insertOne(user);
    }
}
