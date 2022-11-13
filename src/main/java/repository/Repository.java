package repository;

import java.util.List;

public class Repository<T> {
    private Class<T> clz;

    public Repository(Class<T> clz) {
        this.clz = clz;
    }

    public <T> List<T> getItems() {
        SqlConnection mySqlConnection = new SqlConnection();
        return mySqlConnection.getItems(clz);
    }
}
