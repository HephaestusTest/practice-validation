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

    @Mock
    private NotificationService notificationService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, notificationService);
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
    void createUser_sendsWelcomeNotifications() {
        User user = new User("new@example.com", "New User", UserRole.USER);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser("new@example.com", "New User");

        verify(notificationService).sendWelcomeEmail(user);
        verify(notificationService).sendSlackNotification(eq("#new-users"), contains("New User"));
    }

    @Test
    void createUser_notificationFailure_doesNotFailCreation() {
        User user = new User("new@example.com", "New User", UserRole.USER);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        doThrow(new RuntimeException("Slack down")).when(notificationService)
            .sendSlackNotification(anyString(), anyString());

        // Should not throw despite notification failure
        User result = userService.createUser("new@example.com", "New User");
        assertThat(result).isNotNull();
    }

    @Test
    void deleteUser_existingUser_deletesSuccessfully() {
        User user = new User("alice@example.com", "Alice", UserRole.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatCode(() -> userService.deleteUser(1L)).doesNotThrowAnyException();
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_nonExistent_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(99L))
            .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void promoteToAdmin_setsAdminRole() {
        User user = new User("alice@example.com", "Alice", UserRole.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.promoteToAdmin(1L);

        assertThat(result.getRole()).isEqualTo(UserRole.ADMIN);
    }
}
