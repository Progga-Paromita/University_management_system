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
public class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Student can access students endpoint")
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void studentCanAccessStudentEndpoint() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Admin can also access students endpoint")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanAccessStudentEndpoint() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teacher is forbidden from accessing students endpoint")
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void teacherCannotAccessStudentEndpoint() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Student can enroll a new student")
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void studentCanCreateStudent() throws Exception {
        String jsonPayload = """
                {
                    "username": "new_student_test",
                    "fullName": "New Student Test",
                    "email": "new_student_test@kuet.ac.bd",
                    "password": "StudentTest123!"
                }
                """;

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("new_student_test"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }
}
