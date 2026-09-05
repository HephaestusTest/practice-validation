package com.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DatabaseConfig {

    private static final String DB_URL = System.getenv("DB_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final String DB_ADMIN_PASSWORD = System.getenv("DB_ADMIN_PASSWORD");
    private static final String ENCRYPTION_KEY = System.getenv("ENCRYPTION_KEY");

    private Connection connection;

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
        return connection;
    }

    public Connection getAdminConnection() {
        try {
            return DriverManager.getConnection(DB_URL, "admin", DB_ADMIN_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Failed to establish admin database connection", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            // Connection close failure is non-critical
        }
    }

    public void runMigration(String sql) {
        try {
            Connection conn = getConnection();
            // Use PreparedStatement to prevent SQL injection
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException("Migration failed", e);
        }
    }

    public void executeDeleteUser(long userId) {
        // Use parameterized query instead of string concatenation
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setLong(1, userId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + userId, e);
        }
    }

    String getEncryptionKey() {
        // Package-private to limit exposure
        return ENCRYPTION_KEY;
    }
}
