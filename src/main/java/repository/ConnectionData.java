package repository;

class ConnectionData {
    String databaseName;
    String user;
    String password;

    public ConnectionData(String databaseName, String user, String password) {
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
    }

    public ConnectionData() {
        this.databaseName = "test";
        this.user = "root";
        this.password = "1234";
    }
}