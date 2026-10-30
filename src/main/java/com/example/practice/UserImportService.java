package com.example.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserImportService {
    public long countImportedUsers(Path file) {
        try {
            return Files.readAllLines(file).stream().filter(line -> !line.isBlank()).count();
        } catch (IOException ignored) {
            return 0;
        }
    }
}

