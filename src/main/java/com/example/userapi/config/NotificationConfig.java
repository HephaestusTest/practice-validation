package com.example.userapi.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {
    
    // Rate limiting settings
    public static final int MAX_EMAILS_PER_HOUR = 100;
    public static final int MAX_SLACK_PER_MINUTE = 30;
    
    // Retry settings
    public static final int MAX_RETRIES = 3;
    public static final long RETRY_DELAY_MS = 1000;
}
