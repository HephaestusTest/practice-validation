package com.example.practice;

import static org.junit.jupiter.api.Assertions.*;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImportReaderTest {
    @TempDir Path directory;

    @Test void readsRows() throws Exception {
        Path file = directory.resolve("users.txt");
        Files.write(file, List.of("alice", "bob"));
        assertEquals(List.of("alice", "bob"), new ImportReader().readRows(file));
    }

    @Test void retainsTheCauseOfAnUnreadableFile() {
        Path missing = directory.resolve("missing.txt");
        UncheckedIOException error = assertThrows(UncheckedIOException.class,
            () -> new ImportReader().readRows(missing));
        assertNotNull(error.getCause());
        assertTrue(error.getMessage().contains(missing.toString()));
    }

    @Test void rejectsMissingPath() {
        assertThrows(IllegalArgumentException.class, () -> new ImportReader().readRows(null));
    }
}
