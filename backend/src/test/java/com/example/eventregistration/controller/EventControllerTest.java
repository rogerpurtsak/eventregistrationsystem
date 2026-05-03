package com.example.eventregistration.controller;

import com.example.eventregistration.entity.EventItem;
import com.example.eventregistration.repository.EventItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventItemRepository eventItemRepository;

    @BeforeEach
    void setUp() {
        eventItemRepository.deleteAll();
    }

    @Test
    void getEvents_noEvents_returnsEmptyList() throws Exception {
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getEvents_withEvents_returnsList() throws Exception {
        EventItem event = new EventItem();
        event.setTitle("Jooksuvõistlus");
        event.setEventTime(LocalDateTime.of(2026, 8, 1, 10, 0));
        event.setMaxParticipants(100);
        eventItemRepository.save(event);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Jooksuvõistlus"))
                .andExpect(jsonPath("$[0].maxParticipants").value(100))
                .andExpect(jsonPath("$[0].registeredCount").value(0))
                .andExpect(jsonPath("$[0].availableSpots").value(100));
    }

    @Test
    void registerToEvent_valid_returns201() throws Exception {
        EventItem event = new EventItem();
        event.setTitle("Konverents");
        event.setEventTime(LocalDateTime.of(2026, 9, 1, 10, 0));
        event.setMaxParticipants(50);
        EventItem saved = eventItemRepository.save(event);

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jaan","lastName":"Tamm","personalCode":"38001011234"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void registerToEvent_eventNotFound_returns404() throws Exception {
        mockMvc.perform(post("/api/events/999/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jaan","lastName":"Tamm","personalCode":"38001011234"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void registerToEvent_duplicatePersonalCode_returns409() throws Exception {
        EventItem event = new EventItem();
        event.setTitle("Üritus");
        event.setEventTime(LocalDateTime.of(2026, 10, 1, 10, 0));
        event.setMaxParticipants(50);
        EventItem saved = eventItemRepository.save(event);
        String body = """
                {"firstName":"Jaan","lastName":"Tamm","personalCode":"38001011234"}
                """;

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void registerToEvent_eventFull_returns409() throws Exception {
        EventItem event = new EventItem();
        event.setTitle("Väike üritus");
        event.setEventTime(LocalDateTime.of(2026, 11, 1, 10, 0));
        event.setMaxParticipants(1);
        EventItem saved = eventItemRepository.save(event);

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jaan","lastName":"Tamm","personalCode":"38001011234"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Mari","lastName":"Mets","personalCode":"49001011234"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void registerToEvent_invalidPersonalCode_returns400() throws Exception {
        EventItem event = new EventItem();
        event.setTitle("Üritus");
        event.setEventTime(LocalDateTime.of(2026, 12, 1, 10, 0));
        event.setMaxParticipants(50);
        EventItem saved = eventItemRepository.save(event);

        mockMvc.perform(post("/api/events/" + saved.getId() + "/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"firstName":"Jaan","lastName":"Tamm","personalCode":"123"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
