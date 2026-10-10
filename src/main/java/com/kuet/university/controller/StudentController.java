
package com.kuet.university.controller;

import com.kuet.university.entity.User;
import com.kuet.university.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final UserService service;

    public StudentController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(user, User.Role.STUDENT));
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll(User.Role.STUDENT);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return service.getById(id, User.Role.STUDENT);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id,
                       @RequestBody User user) {
        return service.update(id, user, User.Role.STUDENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id, User.Role.STUDENT);
        return ResponseEntity.noContent().build();
    }
}
