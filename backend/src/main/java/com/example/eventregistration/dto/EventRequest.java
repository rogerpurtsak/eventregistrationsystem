package com.example.eventregistration.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventRequest {
    @NotBlank
    public String title;
    @NotNull
    public LocalDateTime eventTime;
    @NotNull
    @Min(1)
    public Integer maxParticipants;
    
}
