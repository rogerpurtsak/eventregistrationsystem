package com.example.eventregistration.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.eventregistration.dto.LoginRequest;
import com.example.eventregistration.dto.LoginResponse;

@Service
public class AuthService {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private String activeToken = null;

    public LoginResponse login(LoginRequest request) {
        if (!adminEmail.equals(request.email) || !adminPassword.equals(request.password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
        }
        activeToken = UUID.randomUUID().toString();
        LoginResponse response = new LoginResponse();
        response.token = activeToken;
        return response;
    }

    public void validateToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header.");
        }
        String token = authHeader.substring(7);
        if (activeToken == null || !activeToken.equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token.");
        }
    }
}
