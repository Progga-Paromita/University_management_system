package com.kuet.university.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/ui/admins")
    @PreAuthorize("hasRole('ADMIN')")
    public String admins() {
        return "admins";
    }

    @GetMapping("/ui/teachers")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public String teachers() {
        return "teachers";
    }

    @GetMapping("/ui/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public String students() {
        return "students";
    }
}
