package com.example.userapi.config;

import java.time.Duration;

/** A dependency-free smoke check for the retry configuration. */
public final class RetryPolicySmokeTest {
    public static void main(String[] args) {
        if (RetryPolicy.maxAttempts("3") != 3) {
            throw new AssertionError("Expected three attempts");
        }
        for (String invalid : new String[] {null, "", "bad", "0", "-1", "2147483648"}) {
            try {
                RetryPolicy.maxAttempts(invalid);
                throw new AssertionError("Expected invalid maximum attempts to be refused: " + invalid);
            } catch (IllegalArgumentException expected) {
                if (expected.getMessage() == null || expected.getMessage().isBlank()) {
                    throw new AssertionError("Expected a descriptive failure", expected);
                }
            }
        }
        if (!RetryPolicy.backoff("250").equals(Duration.ofMillis(250))) {
            throw new AssertionError("Expected a 250 ms delay");
        }
    }
}
