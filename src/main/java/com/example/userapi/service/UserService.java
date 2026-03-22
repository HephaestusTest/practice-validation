package com.example.userapi.service;

import com.example.userapi.exception.DuplicateEmailException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.exception.UserValidationException;
import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import com.example.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        if (id == null) {
            throw new UserValidationException("id", "User ID must not be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("User lookup failed: no user found with id={}", id);
                    return new UserNotFoundException("User not found: id=" + id);
                });
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserValidationException("email", "Email must not be blank");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User lookup failed: no user found with email={}", email);
                    return new UserNotFoundException("User not found: email=" + email);
                });
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(String email, String displayName) {
        validateEmail(email);
        validateDisplayName(displayName);

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already in use: " + email);
        }

        User user = new User(email, displayName, UserRole.USER);
        log.info("Creating user: email={}, displayName={}", email, displayName);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        log.info("Deleted user: id={}, email={}", id, user.getEmail());
    }

    @Transactional
    public User promoteToAdmin(Long id) {
        User user = findById(id);
        if (user.getRole() == UserRole.ADMIN) {
            log.info("User id={} is already an admin, skipping promotion", id);
            return user;
        }
        user.setRole(UserRole.ADMIN);
        log.info("Promoted user to admin: id={}, email={}", id, user.getEmail());
        return userRepository.save(user);
    }

    // ---- private helpers ----

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new UserValidationException("email", "Email must not be blank");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserValidationException("email", "Invalid email format: " + email);
        }
    }

    private void validateDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new UserValidationException("displayName", "Display name must not be blank");
        }
        if (displayName.length() > 100) {
            throw new UserValidationException("displayName",
                    "Display name must not exceed 100 characters");
        }
    }
}
