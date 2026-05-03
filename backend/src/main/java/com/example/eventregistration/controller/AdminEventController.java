package com.example.eventregistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eventregistration.dto.EventRequest;
import com.example.eventregistration.dto.EventResponse;
import com.example.eventregistration.service.AuthService;
import com.example.eventregistration.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminEventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AuthService authService;

    @PostMapping("/events")
    public EventResponse createEvent(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody @Valid EventRequest request) {
        authService.validateToken(authHeader);
        return eventService.createEvent(request);
    }
}
