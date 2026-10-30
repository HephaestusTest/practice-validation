package com.example.practice;

public class PasswordPolicy {
    public boolean accepts(String password) {
        return password != null && password.length() >= 12
            && password.chars().anyMatch(Character::isDigit)
            && password.chars().anyMatch(Character::isUpperCase);
    }
}
