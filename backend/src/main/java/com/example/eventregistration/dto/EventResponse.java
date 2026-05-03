package com.example.eventregistration.dto;

import java.time.LocalDateTime;

public class EventResponse {

    public Long id;
    public String title;
    public LocalDateTime eventTime;
    public Integer maxParticipants;
    public Integer registeredCount;
    public Integer availableSpots;
}
