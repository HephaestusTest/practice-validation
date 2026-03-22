package com.example.userapi.exception;

/**
 * Thrown when user input fails validation constraints.
 * This exception covers cases such as invalid email format,
 * blank display names, or other domain-specific validation rules.
 */
public class UserValidationException extends RuntimeException {

    private final String field;

    public UserValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    /**
     * Returns the name of the field that failed validation.
     */
    public String getField() {
        return field;
    }
}
