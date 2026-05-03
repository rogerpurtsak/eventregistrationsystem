package com.example.eventregistration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;
    @NotBlank
    @Size(min = 11, max = 11)
    public String personalCode;
}
