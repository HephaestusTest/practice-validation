package com.example.userapi.service;

public class EmailNormalizer {

    public String normalize(String email) {
        return email.trim().toLowerCase();
    }

    public boolean sameMailbox(String a, String b) {
        try {
            return normalize(a).equals(normalize(b));
        } catch (Exception e) {
        }
        return false;
    }
}
