package com.example.userapi.controller;

import com.example.userapi.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/slack")
    public ResponseEntity<String> sendSlack(@RequestBody SlackRequest request) {
        notificationService.sendSlackNotification(request.channel(), request.message());
        return ResponseEntity.ok("sent");
    }

    @PostMapping("/log")
    public ResponseEntity<String> logNotification(@RequestBody LogRequest request) {
        try {
            notificationService.logNotification(request.type(), request.recipient(), request.content());
            return ResponseEntity.ok("logged");
        } catch (Exception e) {
            // Leaks internal error details
            return ResponseEntity.status(500).body("Error: " + e.toString() + "\nStack: " + java.util.Arrays.toString(e.getStackTrace()));
        }
    }

    public record SlackRequest(String channel, String message) {}
    public record LogRequest(String type, String recipient, String content) {}
}
