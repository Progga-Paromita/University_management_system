package com.kuet.university.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionTest {

    @Test
    void shouldEncryptPassword1() {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "12345";
        String encodedPassword = encoder.encode(password);

        assertNotEquals(password, encodedPassword);

        assertTrue(
                encoder.matches(password, encodedPassword)
        );
    }
    @Test
    void shouldEncryptPassword2() {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        String password = "ABCDE";
        String encodedPassword = encoder.encode(password);


        //JUnit dependency

        assertNotEquals(password, encodedPassword);

        assertTrue(
                encoder.matches(password, encodedPassword)
        );
    }
}