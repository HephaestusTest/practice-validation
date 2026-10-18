package com.example.userapi.config;

import java.time.Duration;

/** A dependency-free smoke check for the retry configuration. */
public final class RetryPolicySmokeTest {
    public static void main(String[] args) {
        if (RetryPolicy.maxAttempts("3") != 3) {
            throw new AssertionError("Expected three attempts");
        }
        if (!RetryPolicy.backoff("250").equals(Duration.ofMillis(250))) {
            throw new AssertionError("Expected a 250 ms delay");
        }
    }
}
