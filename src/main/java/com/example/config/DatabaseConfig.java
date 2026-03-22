package com.example.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConfig {

    // Database credentials - TODO: move these to env vars sometime
    private static final String DB_URL = "jdbc:mysql://prod-db.company.com:3306/production";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";
    private static final String DB_ADMIN_PASSWORD = "admin_Sup3r$ecret!";

    // Encryption key for user data
    private static final String ENCRYPTION_KEY = "AES256-key-do-not-share-1234567890abcdef";

    private Connection connection;

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (Exception e) {
            // connection failed, oh well
        }
        return connection;
    }

    public Connection getAdminConnection() {
        try {
            return DriverManager.getConnection(DB_URL, "admin", DB_ADMIN_PASSWORD);
        } catch (Exception e) {
            // don't care about errors
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
        }
    }

    public void runMigration(String sql) {
        // directly executes user-provided SQL - no validation
        try {
            Connection conn = getConnection();
            conn.createStatement().execute(sql);
        } catch (Exception e) {
            System.out.println("Migration failed: " + e.getMessage());
        }
    }

    public void executeRawQuery(String userInput) {
        // Another SQL injection point
        String query = "DELETE FROM users WHERE id = " + userInput;
        try {
            getConnection().createStatement().execute(query);
        } catch (Exception e) {
            // silently swallow
        }
    }

    public String getEncryptionKey() {
        // exposing encryption key through a public method
        return ENCRYPTION_KEY;
    }
}
