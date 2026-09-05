package com.example.userapi.service;

import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NotificationServiceTest {

    private final NotificationService notificationService = new NotificationService(
        "https://hooks.slack.com/test/webhook",
        "test-api-key"
    );

    @Test
    void sendWelcomeEmail_handlesFailureGracefully() {
        User user = new User("test@example.com", "Test User", UserRole.USER);

        // Should not throw - logs warning instead
        assertThatCode(() -> notificationService.sendWelcomeEmail(user))
            .doesNotThrowAnyException();
    }

    @Test
    void sendSlackNotification_handlesFailureGracefully() {
        // Should not throw for invalid webhook URL
        assertThatCode(() -> notificationService.sendSlackNotification("#test", "Hello"))
            .doesNotThrowAnyException();
    }
}
