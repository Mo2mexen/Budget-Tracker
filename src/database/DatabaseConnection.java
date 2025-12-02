package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/budget_tracker";
    private static final String USER = "root";
    private static final String PASS = "Aa20052005";

    // Get database connection
    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Database connected");
            return conn;
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}
