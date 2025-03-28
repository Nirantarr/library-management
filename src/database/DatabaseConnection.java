package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/library";
    private static final String USER = "root";
    private static final String PASSWORD = "redhat";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Add the JDBC driver to your project.");
        }
    }
    public static void main(String[] args) {
        System.out.println("Starting Database Connection Program...");

        try {
            Connection conn = getConnection();
            System.out.println(" Attempting to connect to the database...");
            System.out.println(" Database connected successfully!");

            conn.close();
        } catch (SQLException e) {
            System.out.println(" Database connection failed! Error: " + e.getMessage());

            e.printStackTrace();
        }
    }
}
