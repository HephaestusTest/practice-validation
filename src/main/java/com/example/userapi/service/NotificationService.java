package com.example.userapi.service;

import com.example.userapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final String slackWebhookUrl;
    private final String sendgridApiKey;

    public NotificationService(
        @Value("${notification.slack.webhook-url}") String slackWebhookUrl,
        @Value("${notification.sendgrid.api-key}") String sendgridApiKey
    ) {
        this.slackWebhookUrl = slackWebhookUrl;
        this.sendgridApiKey = sendgridApiKey;
    }

    public void sendWelcomeEmail(User user) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(
                "https://api.sendgrid.com/v3/mail/send"
            ).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + sendgridApiKey);
            conn.setDoOutput(true);

            String json = "{\"to\":\"" + user.getEmail() + "\",\"subject\":\"Welcome!\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int status = conn.getResponseCode();
            if (status != 200) {
                log.warn("Welcome email failed: userId={}, status={}", user.getId(), status);
            }
        } catch (IOException e) {
            log.warn("Failed to send welcome email: userId={}", user.getId(), e);
        }
    }

    public void sendSlackNotification(String channel, String message) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(slackWebhookUrl).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String payload = "{\"channel\":\"" + channel + "\",\"text\":\"" + message + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }
            int status = conn.getResponseCode();
            if (status != 200) {
                log.warn("Slack notification failed: channel={}, status={}", channel, status);
            }
        } catch (IOException e) {
            log.warn("Failed to send Slack notification: channel={}", channel, e);
        }
    }
}
