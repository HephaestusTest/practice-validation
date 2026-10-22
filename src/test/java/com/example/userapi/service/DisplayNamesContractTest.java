package com.example.userapi.service;

/** Dependency-free contract checks; run this class with the JDK, without Maven. */
public final class DisplayNamesContractTest {
    public static void main(String[] args) {
        equal("Alice", DisplayNames.normalize("Alice"));
        equal("Alice Example", DisplayNames.normalize("  Alice   Example  "));
        equal("Alice Example", DisplayNames.normalize("\tAlice\n\rExample\t"));
        equal("Zoë García", DisplayNames.normalize("  Zoë García  "));
        equal("Alice Example", DisplayNames.normalize("\u00a0Alice\u2003Example\u00a0"));
        equal("A".repeat(80), DisplayNames.normalize("  " + "A".repeat(80) + "  "));
        equal("😀".repeat(80), DisplayNames.normalize("😀".repeat(80)));
        rejected(null, "Display name is required");
        rejected("", "Display name is required");
        rejected(" \t\n\u00a0", "Display name is required");
        rejected("A".repeat(81), "Display name must have at most 80 characters");
        rejected("😀".repeat(81), "Display name must have at most 80 characters");
        System.out.println("DisplayNames contract: 12 cases passed");
    }

    private static void equal(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected [" + expected + "], got [" + actual + "]");
        }
    }

    private static void rejected(String value, String expectedMessage) {
        try {
            DisplayNames.normalize(value);
        } catch (IllegalArgumentException exception) {
            equal(expectedMessage, exception.getMessage());
            return;
        }
        throw new AssertionError("Expected invalid display name to be rejected");
    }
}
