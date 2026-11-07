package com.kuet.university.service;

import com.kuet.university.entity.User;
import com.kuet.university.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class DuplicateEmailTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User newUser;
    private User existingUser;

    @BeforeEach
    void setUp() {
        newUser = new User();
        newUser.setUsername("newuser");
        newUser.setFullName("New User");
        newUser.setEmail("duplicate@kuet.ac.bd");
        newUser.setPassword("Password123!");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("existinguser");
        existingUser.setFullName("Existing User");
        existingUser.setEmail("existing@kuet.ac.bd");
        existingUser.setRole(User.Role.TEACHER);
    }

    @Test
    @DisplayName("Should throw CONFLICT when creating user with an already registered email")
    void shouldThrowConflictWhenCreatingUserWithDuplicateEmail() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("duplicate@kuet.ac.bd")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.create(newUser, User.Role.STUDENT)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Email already exists"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw CONFLICT when updating user with another user's email")
    void shouldThrowConflictWhenUpdatingUserWithDuplicateEmail() {
        User updateInput = new User();
        updateInput.setEmail("taken@kuet.ac.bd");

        when(userRepository.findByIdAndRole(1L, User.Role.TEACHER)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@kuet.ac.bd")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.update(1L, updateInput, User.Role.TEACHER)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Email already exists"));
        verify(userRepository, never()).save(any(User.class));
    }
}
