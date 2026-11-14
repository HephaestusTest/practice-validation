package com.example.practice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserImportService {
    public long countImportedUsers(Path file) {
        try {
            return Files.readAllLines(file).stream().filter(line -> !line.isBlank()).count();
        } catch (IOException e) {
            throw new java.io.UncheckedIOException("Could not read import file: " + file, e);
        }
    }
}

// qual-bench qual-pass2-d5-0
// qual-bench round1-2
