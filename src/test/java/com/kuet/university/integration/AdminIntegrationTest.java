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
public class AdminIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Admin can view list of administrators")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanGetAdminList() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Teacher is forbidden from accessing admin endpoints")
    @WithMockUser(username = "teacher", roles = {"TEACHER"})
    void teacherCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Student is forbidden from accessing admin endpoints")
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void studentCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin can create a new administrator")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanCreateAdmin() throws Exception {
        String jsonPayload = """
                {
                    "username": "super_admin_test",
                    "fullName": "Super Admin Test",
                    "email": "super_admin_test@kuet.ac.bd",
                    "password": "AdminTest123!"
                }
                """;

        mockMvc.perform(post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("super_admin_test"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }
}
