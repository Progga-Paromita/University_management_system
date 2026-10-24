package com.kuet.university.config;

import com.kuet.university.entity.User;
import com.kuet.university.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository repository,
            PasswordEncoder encoder) {

        return args -> {
            createUser(repository, encoder, "admin",
                    "Admin123!", "University Admin",
                    "admin@kuet.ac.bd", User.Role.ADMIN);

            createUser(repository, encoder, "teacher",
                    "Teacher123!", "Demo Teacher",
                    "teacher@kuet.ac.bd", User.Role.TEACHER);

            createUser(repository, encoder, "student",
                    "Student123!", "Demo Student",
                    "student@kuet.ac.bd", User.Role.STUDENT);
        };
    }

    private void createUser(
            UserRepository repository,
            PasswordEncoder encoder,
            String username,
            String password,
            String fullName,
            String email,
            User.Role role) {

        if (!repository.existsByUsername(username) && !repository.existsByEmail(email)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(password));
            user.setFullName(fullName);
            user.setEmail(email);
            user.setRole(role);
            repository.save(user);
        }
    }
}
