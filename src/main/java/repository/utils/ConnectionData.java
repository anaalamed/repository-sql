package repository.utils;

public class ConnectionData {
    private String databaseName;
    private String user;
    private String password;

    public ConnectionData(String databaseName, String user, String password) {
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
    }

    public ConnectionData() {
        this.databaseName = "test";
        this.user = "root";
        this.password = "";
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}