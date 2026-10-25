package com.kuet.university.controller;

import com.kuet.university.entity.User;
import com.kuet.university.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(user, User.Role.ADMIN));
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll(User.Role.ADMIN);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id, User.Role.ADMIN);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @RequestBody User user) {
        return service.update(id, user, User.Role.ADMIN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, User.Role.ADMIN);
        return ResponseEntity.noContent().build();
    }
}
