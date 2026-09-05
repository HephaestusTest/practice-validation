package com.example.userapi.service;

import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.exception.DuplicateEmailException;
import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import com.example.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(String email, String displayName) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already in use: " + email);
        }
        User user = new User(email, displayName, UserRole.USER);
        log.info("Creating user: email={}", email);
        User saved = userRepository.save(user);
        
        // Send welcome notification
        try {
            notificationService.sendWelcomeEmail(saved);
            notificationService.sendSlackNotification("#new-users", "New user: " + displayName);
        } catch (Exception e) {
            // don't fail user creation for notification issues
        }
        
        return saved;
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
        user.setRole(UserRole.ADMIN);
        log.info("Promoted user to admin: id={}", id);
        return userRepository.save(user);
    }
}
