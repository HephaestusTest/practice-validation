package com.example.userapi.service;

import com.example.userapi.exception.DuplicateEmailException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.exception.UserValidationException;
import com.example.userapi.model.User;
import com.example.userapi.model.UserRole;
import com.example.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    // ---- findById ----

    @Nested
    class FindById {

        @Test
        void existingUser_returnsUser() {
            User user = new User("alice@example.com", "Alice", UserRole.USER);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            User result = userService.findById(1L);

            assertThat(result.getEmail()).isEqualTo("alice@example.com");
            assertThat(result.getDisplayName()).isEqualTo("Alice");
        }

        @Test
        void nonExistentUser_throwsUserNotFoundException() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findById(99L))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        void nullId_throwsUserValidationException() {
            assertThatThrownBy(() -> userService.findById(null))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessageContaining("must not be null");
        }
    }

    // ---- findByEmail ----

    @Nested
    class FindByEmail {

        @Test
        void existingEmail_returnsUser() {
            User user = new User("bob@example.com", "Bob", UserRole.USER);
            when(userRepository.findByEmail("bob@example.com")).thenReturn(Optional.of(user));

            User result = userService.findByEmail("bob@example.com");

            assertThat(result.getDisplayName()).isEqualTo("Bob");
        }

        @Test
        void nonExistentEmail_throwsUserNotFoundException() {
            when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findByEmail("missing@example.com"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("missing@example.com");
        }

        @ParameterizedTest
        @NullAndEmptySource
        void blankOrNullEmail_throwsValidationException(String email) {
            assertThatThrownBy(() -> userService.findByEmail(email))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessageContaining("must not be blank");
        }
    }

    // ---- findAll ----

    @Nested
    class FindAll {

        @Test
        void returnsAllUsers() {
            List<User> users = List.of(
                    new User("a@example.com", "A", UserRole.USER),
                    new User("b@example.com", "B", UserRole.ADMIN)
            );
            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.findAll();

            assertThat(result).hasSize(2);
        }

        @Test
        void noUsers_returnsEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<User> result = userService.findAll();

            assertThat(result).isEmpty();
        }
    }

    // ---- createUser ----

    @Nested
    class CreateUser {

        @Test
        void validInput_createsAndReturnsUser() {
            when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            User result = userService.createUser("new@example.com", "New User");

            assertThat(result.getEmail()).isEqualTo("new@example.com");
            assertThat(result.getDisplayName()).isEqualTo("New User");
            assertThat(result.getRole()).isEqualTo(UserRole.USER);
            verify(userRepository).save(any(User.class));
        }

        @Test
        void duplicateEmail_throwsDuplicateEmailException() {
            when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser("taken@example.com", "Bob"))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessageContaining("taken@example.com");

            verify(userRepository, never()).save(any());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void blankOrNullEmail_throwsValidationException(String email) {
            assertThatThrownBy(() -> userService.createUser(email, "Valid Name"))
                    .isInstanceOf(UserValidationException.class);

            verify(userRepository, never()).save(any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"not-an-email", "missing@", "@no-local.com", "spaces in@email.com"})
        void invalidEmailFormat_throwsValidationException(String email) {
            assertThatThrownBy(() -> userService.createUser(email, "Valid Name"))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessageContaining("Invalid email format");

            verify(userRepository, never()).save(any());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void blankOrNullDisplayName_throwsValidationException(String displayName) {
            assertThatThrownBy(() -> userService.createUser("valid@example.com", displayName))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessageContaining("must not be blank");
        }

        @Test
        void displayNameExceeds100Characters_throwsValidationException() {
            String longName = "A".repeat(101);

            assertThatThrownBy(() -> userService.createUser("valid@example.com", longName))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessageContaining("must not exceed 100 characters");
        }
    }

    // ---- deleteUser ----

    @Nested
    class DeleteUser {

        @Test
        void existingUser_deletesSuccessfully() {
            User user = new User("alice@example.com", "Alice", UserRole.USER);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            userService.deleteUser(1L);

            verify(userRepository).delete(user);
        }

        @Test
        void nonExistentUser_throwsUserNotFoundException() {
            when(userRepository.findById(42L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.deleteUser(42L))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).delete(any());
        }
    }

    // ---- promoteToAdmin ----

    @Nested
    class PromoteToAdmin {

        @Test
        void regularUser_promotedToAdmin() {
            User user = new User("alice@example.com", "Alice", UserRole.USER);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            User result = userService.promoteToAdmin(1L);

            assertThat(result.getRole()).isEqualTo(UserRole.ADMIN);
            verify(userRepository).save(user);
        }

        @Test
        void alreadyAdmin_returnsWithoutSaving() {
            User admin = new User("admin@example.com", "Admin", UserRole.ADMIN);
            when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

            User result = userService.promoteToAdmin(1L);

            assertThat(result.getRole()).isEqualTo(UserRole.ADMIN);
            verify(userRepository, never()).save(any());
        }

        @Test
        void nonExistentUser_throwsUserNotFoundException() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.promoteToAdmin(99L))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }
}
