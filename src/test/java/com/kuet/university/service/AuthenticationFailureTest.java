package com.kuet.university.service;

import com.kuet.university.config.SecurityConfig;
import com.kuet.university.entity.User;
import com.kuet.university.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFailureTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        SecurityConfig config = new SecurityConfig();
        userDetailsService = config.userDetailsService(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist during authentication")
    void shouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown_user")
        );

        verify(userRepository).findByUsername("unknown_user");
    }

    @Test
    @DisplayName("Should fail authentication verification when password does not match encoded hash")
    void shouldFailPasswordMatchWhenWrongPasswordProvided() {
        User user = new User();
        user.setUsername("valid_user");
        user.setPassword(passwordEncoder.encode("CorrectPassword123!"));
        user.setRole(User.Role.STUDENT);

        when(userRepository.findByUsername("valid_user")).thenReturn(Optional.of(user));

        UserDetails loadedUser = userDetailsService.loadUserByUsername("valid_user");
        assertNotNull(loadedUser);

        boolean passwordMatches = passwordEncoder.matches("WrongPassword999!", loadedUser.getPassword());
        assertFalse(passwordMatches, "Authentication should fail for incorrect password");
    }
}
