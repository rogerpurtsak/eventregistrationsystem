package com.example.eventregistration.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventregistration.dto.EventResponse;
import com.example.eventregistration.dto.RegistrationRequest;
import com.example.eventregistration.service.EventService;
import com.example.eventregistration.service.RegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private RegistrationService registrationService;

    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/{eventId}/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerToEvent(@PathVariable Long eventId,
            @RequestBody @Valid RegistrationRequest request) {
        registrationService.registerForEvent(eventId, request);
    }
}
