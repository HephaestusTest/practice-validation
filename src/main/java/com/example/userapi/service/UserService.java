package com.example.userapi.service;

import com.example.userapi.exception.DuplicateEmailException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import com.example.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        validateId(id);
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return List.copyOf(userRepository.findAll());
    }

    @Transactional
    public User createUser(String email, String displayName) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedName = normalizeName(displayName);
        ensureEmailAvailable(normalizedEmail);
        User user = new User(normalizedEmail, normalizedName, UserRole.USER);
        User saved = userRepository.save(user);
        log.info("Created user");
        return saved;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User id must be positive");
        }
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        String result = email.trim().toLowerCase(Locale.ROOT);
        if (!result.contains("@")) {
            throw new IllegalArgumentException("Email must contain @");
        }
        return result;
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Display name is required");
        }
        return name.trim();
    }

    private void ensureEmailAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already in use: " + email);
        }
    }
}
