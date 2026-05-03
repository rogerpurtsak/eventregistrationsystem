package com.example.eventregistration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@example.com","password":"admin123"}
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        return body.replaceAll(".*\"token\":\"([^\"]+)\".*", "$1");
    }

    @Test
    void createEvent_withValidToken_returns200() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"title":"Jooksuvõistlus","eventTime":"2026-08-01T10:00:00","maxParticipants":100}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Jooksuvõistlus"))
                .andExpect(jsonPath("$.maxParticipants").value(100))
                .andExpect(jsonPath("$.availableSpots").value(100));
    }

    @Test
    void createEvent_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Jooksuvõistlus","eventTime":"2026-08-01T10:00:00","maxParticipants":100}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createEvent_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer invalid-token")
                        .content("""
                                {"title":"Jooksuvõistlus","eventTime":"2026-08-01T10:00:00","maxParticipants":100}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createEvent_missingTitle_returns400() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"title":"","eventTime":"2026-08-01T10:00:00","maxParticipants":100}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void createEvent_invalidMaxParticipants_returns400() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(post("/api/admin/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {"title":"Üritus","eventTime":"2026-08-01T10:00:00","maxParticipants":0}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
