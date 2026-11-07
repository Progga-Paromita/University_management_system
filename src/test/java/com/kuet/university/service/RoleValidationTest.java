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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleValidationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User teacherUser;
    private User studentUser;

    @BeforeEach
    void setUp() {
        teacherUser = new User();
        teacherUser.setId(10L);
        teacherUser.setUsername("prof_karim");
        teacherUser.setFullName("Prof. Karim");
        teacherUser.setEmail("karim@kuet.ac.bd");
        teacherUser.setRole(User.Role.TEACHER);

        studentUser = new User();
        studentUser.setId(20L);
        studentUser.setUsername("student_rahim");
        studentUser.setFullName("Rahim Ahmed");
        studentUser.setEmail("rahim@kuet.ac.bd");
        studentUser.setRole(User.Role.STUDENT);
    }

    @Test
    @DisplayName("Should retrieve only teachers when requesting TEACHER role list")
    void shouldReturnOnlyTeachersWhenRequestingTeacherRole() {
        when(userRepository.findByRole(User.Role.TEACHER)).thenReturn(List.of(teacherUser));

        List<User> teachers = userService.getAll(User.Role.TEACHER);

        assertEquals(1, teachers.size());
        assertEquals(User.Role.TEACHER, teachers.get(0).getRole());
        assertEquals("prof_karim", teachers.get(0).getUsername());
        verify(userRepository).findByRole(User.Role.TEACHER);
    }

    @Test
    @DisplayName("Should throw NOT_FOUND when requesting student ID with TEACHER role")
    void shouldThrowNotFoundWhenRoleDoesNotMatch() {
        when(userRepository.findByIdAndRole(20L, User.Role.TEACHER)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.getById(20L, User.Role.TEACHER)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository).findByIdAndRole(20L, User.Role.TEACHER);
    }

    @Test
    @DisplayName("Should enforce role assignment upon user creation")
    void shouldEnforceRoleAssignmentOnCreation() {
        User input = new User();
        input.setUsername("new_admin");
        input.setFullName("New Admin");
        input.setEmail("admin_new@kuet.ac.bd");
        input.setPassword("Admin123!");

        when(userRepository.existsByUsername("new_admin")).thenReturn(false);
        when(userRepository.existsByEmail("admin_new@kuet.ac.bd")).thenReturn(false);
        when(passwordEncoder.encode("Admin123!")).thenReturn("encoded_admin_pwd");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.create(input, User.Role.ADMIN);

        assertNotNull(created);
        assertEquals(User.Role.ADMIN, created.getRole());
        verify(userRepository).save(argThat(user -> user.getRole() == User.Role.ADMIN));
    }
}
