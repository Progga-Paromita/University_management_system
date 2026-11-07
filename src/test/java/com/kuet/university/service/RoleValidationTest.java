package com.kuet.university.service;

import com.kuet.university.entity.User;
import com.kuet.university.entity.User.Role;
import com.kuet.university.repository.UserRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleValidationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldAcceptValidRoles() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.TEACHER, Role.valueOf("TEACHER"));
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
    }

    @Test
    void shouldRejectInvalidRole1() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Role.valueOf("MANAGER")
        );
    }

    @Test
    void shouldRejectInvalidRole2() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Role.valueOf("CUSTOMER")
        );
    }

    @Test
    void shouldRejectInvalidRole3() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Role.valueOf("UNKNOWN")
        );
    }

    @Test
    void shouldReturnOnlyUsersOfRequestedRole() {
        User teacher = new User();
        teacher.setUsername("prof_rahman");
        teacher.setRole(Role.TEACHER);

        when(userRepository.findByRole(Role.TEACHER)).thenReturn(List.of(teacher));

        List<User> result = userService.getAll(Role.TEACHER);

        assertEquals(1, result.size());
        assertEquals(Role.TEACHER, result.get(0).getRole());
        verify(userRepository).findByRole(Role.TEACHER);
    }

    @Test
    void shouldThrowNotFoundWhenUserRoleDoesNotMatch() {
        when(userRepository.findByIdAndRole(10L, Role.TEACHER)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.getById(10L, Role.TEACHER)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository).findByIdAndRole(10L, Role.TEACHER);
    }
}
