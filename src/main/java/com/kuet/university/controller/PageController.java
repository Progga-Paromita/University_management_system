
package com.kuet.university.controller;

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
    public String admins() {
        return "admins";
    }

    @GetMapping("/ui/teachers")
    public String teachers() {
        return "teachers";
    }

    @GetMapping("/ui/students")
    public String students() {
        return "students";
    }
}
