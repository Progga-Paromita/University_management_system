package com.kuet.university.service;

import com.kuet.university.entity.User;
import com.kuet.university.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll(User.Role role) {
        return repository.findByRole(role);
    }

    public User getById(Long id, User.Role role) {
        return repository.findByIdAndRole(id, role)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    public User create(User user, User.Role role) {
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body is required");
        }
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Full name is required");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email is required");
        }

        if (repository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists");
        }

        user.setId(null);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return repository.save(user);
    }

    public User update(Long id, User input, User.Role role) {
        if (input == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Request body is required");
        }
        User user = getById(id, role);

        if (input.getUsername() != null && !input.getUsername().isBlank()) {
            if (!user.getUsername().equals(input.getUsername())
                    && repository.existsByUsername(input.getUsername())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Username already exists");
            }
            user.setUsername(input.getUsername());
        }

        if (input.getEmail() != null && !input.getEmail().isBlank()) {
            if (!user.getEmail().equals(input.getEmail())
                    && repository.existsByEmail(input.getEmail())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "Email already exists");
            }
            user.setEmail(input.getEmail());
        }

        if (input.getFullName() != null && !input.getFullName().isBlank()) {
            user.setFullName(input.getFullName());
        }

        if (input.getPassword() != null
                && !input.getPassword().isBlank()) {
            user.setPassword(
                    passwordEncoder.encode(input.getPassword()));
        }

        return repository.save(user);
    }

    public void delete(Long id, User.Role role) {
        repository.delete(getById(id, role));
    }
}
