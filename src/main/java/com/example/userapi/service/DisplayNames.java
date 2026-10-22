package com.example.userapi.service;

import java.util.regex.Pattern;

/** The display-name form stored for newly created users. */
public final class DisplayNames {
    private static final int MAX_CODE_POINTS = 80;
    private static final Pattern WHITESPACE = Pattern.compile("\\s+", Pattern.UNICODE_CHARACTER_CLASS);

    private DisplayNames() {}

    public static String normalize(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Display name is required");
        }
        String normalized = WHITESPACE.matcher(value).replaceAll(" ").strip();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Display name is required");
        }
        if (normalized.codePointCount(0, normalized.length()) > MAX_CODE_POINTS) {
            throw new IllegalArgumentException("Display name must have at most 80 characters");
        }
        return normalized;
    }
}
