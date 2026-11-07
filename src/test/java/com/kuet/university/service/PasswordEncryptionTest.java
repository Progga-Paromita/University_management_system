package com.kuet.university.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionTest {

    @Test
    void shouldEncryptPassword() {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "123456";
        String encodedPassword = encoder.encode(password);

        assertNotEquals(password, encodedPassword);

        assertTrue(
                encoder.matches(password, encodedPassword)
        );
    }
}