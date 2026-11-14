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
// qual-bench qual-pass2-1
// qual-bench round1-1
// qual-bench round1-1
// qual-bench round2-1
// qual-bench round3-1
// qual-bench qual-d1-0
