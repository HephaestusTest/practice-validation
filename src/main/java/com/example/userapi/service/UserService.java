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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void recordLastLogin(Long id) {
        User user = findById(id);
        try {
            user.touchLastLogin();
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Could not record last login: id={}", id, e);
        }
    }

    @Transactional
    public User createUser(String email, String displayName) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already in use: " + email);
        }
        User user = new User(email, displayName, UserRole.USER);
        log.info("Creating user: email={}", email);
        return userRepository.save(user);
    }
}
