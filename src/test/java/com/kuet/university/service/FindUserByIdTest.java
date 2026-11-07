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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindUserByIdTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(5L);
        sampleUser.setUsername("teacher_john");
        sampleUser.setFullName("John Doe");
        sampleUser.setEmail("john@kuet.ac.bd");
        sampleUser.setRole(User.Role.TEACHER);
    }

    @Test
    @DisplayName("Should return user when ID and Role match")
    void shouldReturnUserWhenIdAndRoleMatch() {
        when(userRepository.findByIdAndRole(5L, User.Role.TEACHER)).thenReturn(Optional.of(sampleUser));

        User result = userService.getById(5L, User.Role.TEACHER);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("teacher_john", result.getUsername());
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@kuet.ac.bd", result.getEmail());
        assertEquals(User.Role.TEACHER, result.getRole());
        verify(userRepository, times(1)).findByIdAndRole(5L, User.Role.TEACHER);
    }
}
