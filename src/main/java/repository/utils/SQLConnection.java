package repository.utils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection implements AutoCloseable {
    Connection connection;

    private SQLConnection(String databaseName, String user, String password)
            throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
    }

    public static SQLConnection createSQLConnection(String filename) throws SQLException, ClassNotFoundException {
        ConnectionData connectionData = parseConfigFile(filename);
        return new SQLConnection(connectionData.getDatabaseName(), connectionData.getUser(), connectionData.getPassword());
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectionData parseConfigFile(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(fileInputStream));
            return gson.fromJson(jsonReader, ConnectionData.class);
        } catch (IOException ex) {
            ConnectionData defaultData = new ConnectionData();
            FileUtils.writeObjectToJsonFile(filename, defaultData);
            return defaultData;
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
