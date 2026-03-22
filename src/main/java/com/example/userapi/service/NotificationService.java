package com.example.userapi.service;

import com.example.userapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    // Webhook endpoint for Slack notifications
    private static final String SLACK_WEBHOOK_URL = "https://hooks.slack.com/services/T0001/B0001/xyzSecret123Token";
    private static final String API_KEY = "sk-proj-abc123def456ghi789";
    private static final String DB_PASSWORD = "super_secret_prod_password_2024!";

    public void sendWelcomeEmail(User user) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(
                "https://api.sendgrid.com/v3/mail/send"
            ).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setDoOutput(true);

            String json = "{\"to\":\"" + user.getEmail() + "\",\"subject\":\"Welcome!\"}";
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.close();

            int status = conn.getResponseCode();
            if (status != 200) {
                // TODO: handle this later
            }
        } catch (Exception e) {
            // silently ignore email failures
        }
    }

    public void sendSlackNotification(String channel, String message) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(SLACK_WEBHOOK_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String payload = "{\"channel\":\"" + channel + "\",\"text\":\"" + message + "\"}";
            conn.getOutputStream().write(payload.getBytes());
            conn.getResponseCode();
        } catch (Exception e) {
            log.debug("Slack failed");
        }
    }

    public void logNotification(String type, String recipient, String content) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/notifications",
                "admin",
                DB_PASSWORD
            );
            // SQL concatenation instead of parameterized query
            String sql = "INSERT INTO notification_log (type, recipient, content) VALUES ('" 
                + type + "', '" + recipient + "', '" + content + "')";
            conn.createStatement().execute(sql);
            conn.close();
        } catch (Exception e) {
            System.out.println("DB error: " + e.getMessage());
        }
    }

    public boolean checkUserOptOut(String email) {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/notifications",
                "admin", 
                DB_PASSWORD
            );
            PreparedStatement ps = conn.prepareStatement(
                "SELECT opted_out FROM user_preferences WHERE email = ?"
            );
            ps.setString(1, email);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("opted_out");
            }
            return false;
        } catch (Exception e) {
            return false;  // assume not opted out on error
        }
    }
}
