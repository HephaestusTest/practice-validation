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

    public record SlackRequest(String channel, String message) {}
}
