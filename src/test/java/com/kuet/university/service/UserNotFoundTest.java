package com.kuet.university.service;

import com.kuet.university.entity.User;
import com.kuet.university.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserNotFoundTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should throw NOT_FOUND when querying a non-existent user ID")
    void shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.findByIdAndRole(999L, User.Role.STUDENT)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.getById(999L, User.Role.STUDENT)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("User not found"));
    }

    @Test
    @DisplayName("Should throw NOT_FOUND when attempting to delete a non-existent user")
    void shouldThrowNotFoundWhenDeletingNonExistentUser() {
        when(userRepository.findByIdAndRole(999L, User.Role.ADMIN)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.delete(999L, User.Role.ADMIN)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository, never()).delete(any(User.class));
    }
}
