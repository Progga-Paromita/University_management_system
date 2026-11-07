package com.kuet.university.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Teacher can access teachers endpoint")
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void teacherCanAccessTeacherEndpoint() throws Exception {
        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Admin can also access teachers endpoint")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessTeacherEndpoint() throws Exception {
        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Student is forbidden from accessing teachers endpoint")
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void studentCannotAccessTeacherEndpoint() throws Exception {
        mockMvc.perform(get("/teachers"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Teacher can register a new teacher")
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void teacherCanCreateTeacher() throws Exception {
        String jsonPayload = """
                {
                    "username": "new_teacher_test",
                    "fullName": "New Teacher Test",
                    "email": "new_teacher_test@kuet.ac.bd",
                    "password": "TeacherTest123!"
                }
                """;

        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new_teacher_test"))
                .andExpect(jsonPath("$.role").value("TEACHER"));
    }
}
