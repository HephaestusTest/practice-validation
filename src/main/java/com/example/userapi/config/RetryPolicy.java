package com.example.userapi.config;

import java.time.Duration;

/** Reads the retry limits used by the user-directory client. */
public final class RetryPolicy {
    private RetryPolicy() {}

    public static int maxAttempts(String configured) {
        return Integer.parseInt(configured);
    }

    public static Duration backoff(String configuredMillis) {
        try {
            return Duration.ofMillis(Long.parseLong(configuredMillis));
        } catch (RuntimeException exception) {
            return Duration.ZERO;
        }
    }
}
