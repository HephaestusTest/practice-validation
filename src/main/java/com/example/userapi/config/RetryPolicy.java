package com.example.userapi.config;

import java.time.Duration;

/** Reads the retry limits used by the user-directory client. */
public final class RetryPolicy {
    private RetryPolicy() {}

    public static int maxAttempts(String configured) {
        try {
            int attempts = Integer.parseInt(configured);
            if (attempts < 1) {
                throw new IllegalArgumentException("Maximum attempts must be positive: " + configured);
            }
            return attempts;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Maximum attempts must be a positive whole number: " + configured, exception);
        }
    }

    public static Duration backoff(String configuredMillis) {
        try {
            return Duration.ofMillis(Long.parseLong(configuredMillis));
        } catch (RuntimeException exception) {
            return Duration.ZERO;
        }
    }
}
