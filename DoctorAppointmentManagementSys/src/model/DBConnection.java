package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final String url = "jdbc:mysql://localhost:3308/medicare";
    private final String user = "root";
    private final String password = "2026";

    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Database Connected.");
            return conn;
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed.");
            e.printStackTrace();
            return null;
        }
    }
}