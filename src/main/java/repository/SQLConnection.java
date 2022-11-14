package repository;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLConnection {
    private static SQLConnection single_instance = null;
    Connection connection;

    private SQLConnection(String databaseName, String user, String password)
            throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
    }

    public static SQLConnection getInstance(String databaseName, String user, String password)
            throws SQLException, ClassNotFoundException {
        if(single_instance == null) {
            single_instance = new SQLConnection(databaseName, user, password);
        }

        return single_instance;
    }

    public static SQLConnection getInstance(String filename) throws SQLException, ClassNotFoundException {
        ConnectionData connectionData = parseConfigFile(filename);
        return getInstance(connectionData.databaseName, connectionData.user, connectionData.password);
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectionData parseConfigFile(String filename) {
        final File folder = new File(filename);

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
}
