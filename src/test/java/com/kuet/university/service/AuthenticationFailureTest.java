package com.kuet.university.service;

import com.kuet.university.config.SecurityConfig;
import com.kuet.university.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationFailureTest {

    @Test
    void shouldRejectInvalidCredentials() {
        // Mock UserRepository
        UserRepository repository = mock(UserRepository.class);

        // Create UserDetailsService from SecurityConfig
        SecurityConfig securityConfig = new SecurityConfig();
        UserDetailsService userDetailsService = securityConfig.userDetailsService(repository);

        // Return empty result for unknown username
        when(repository.findByUsername("admin11")).thenReturn(Optional.empty());

        // Verify authentication user lookup fails
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("admin11")
        );

        // Verify repository method was called with the searched username
        verify(repository).findByUsername("admin11");
    }

    @Test
    void shouldFailPasswordMatchWhenWrongPasswordProvided() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "CorrectPassword123!";
        String encodedPassword = encoder.encode(rawPassword);

        // Check correct password matches
        assertTrue(encoder.matches(rawPassword, encodedPassword));

        // Check wrong password fails
        assertFalse(encoder.matches("WrongPassword999!", encodedPassword));
    }
}
