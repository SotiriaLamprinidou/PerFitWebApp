package config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConfig {
    private static final String PROPERTIES_FILE = "config.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // Static block loads DB configuration from properties file once
    static {
        try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            if (input == null) throw new RuntimeException("config.properties not found");
            prop.load(input);
            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("Error loading DB configuration", e);
        }
    }

    // Establishes and returns a connection to the MySQL database
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Ensures MySQL driver is loaded
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
