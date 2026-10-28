package com.example.userapi.service;

import com.example.userapi.exception.DuplicateEmailException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import com.example.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void findById_existingUser_returnsUser() {
        User user = new User("alice@example.com", "Alice", UserRole.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findById_nonExistent_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(99L))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    void createUser_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser("taken@example.com", "Bob"))
            .isInstanceOf(DuplicateEmailException.class);
    }
    @Test
    void countUsers_twoSavedUsers_returnsTwo() {
        var users = new java.util.ArrayList<User>();
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            users.add(user);
            return user;
        });
        when(userRepository.count()).thenAnswer(invocation -> (long) users.size());
        userService.createUser("alice@example.com", "Alice");
        userService.createUser("bob@example.com", "Bob");
        assertThat(userService.countUsers()).isEqualTo(2);
    }

}
