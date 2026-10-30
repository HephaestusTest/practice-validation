package com.example.practice;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImportReader {
    public List<String> readRows(Path file) {
        if (file == null) throw new IllegalArgumentException("Import path is required");
        try {
            return Files.readAllLines(file);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read import file: " + file, e);
        }
    }
}
