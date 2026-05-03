package com.example.eventregistration.service;

import com.example.eventregistration.dto.LoginRequest;
import com.example.eventregistration.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "adminEmail", "admin@example.com");
        ReflectionTestUtils.setField(authService, "adminPassword", "admin123");
    }

    @Test
    void login_correctCredentials_returnsToken() {
        LoginRequest request = new LoginRequest();
        request.email = "admin@example.com";
        request.password = "admin123";

        LoginResponse response = authService.login(request);

        assertNotNull(response.token);
        assertFalse(response.token.isBlank());
    }

    @Test
    void login_wrongPassword_throwsUnauthorized() {
        LoginRequest request = new LoginRequest();
        request.email = "admin@example.com";
        request.password = "wrong";

        assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

    @Test
    void login_wrongEmail_throwsUnauthorized() {
        LoginRequest request = new LoginRequest();
        request.email = "other@example.com";
        request.password = "admin123";

        assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

    @Test
    void validateToken_validToken_doesNotThrow() {
        LoginRequest request = new LoginRequest();
        request.email = "admin@example.com";
        request.password = "admin123";
        LoginResponse loginResponse = authService.login(request);

        assertDoesNotThrow(() -> authService.validateToken("Bearer " + loginResponse.token));
    }

    @Test
    void validateToken_invalidToken_throwsUnauthorized() {
        assertThrows(ResponseStatusException.class,
                () -> authService.validateToken("Bearer invalid-token"));
    }

    @Test
    void validateToken_missingHeader_throwsUnauthorized() {
        assertThrows(ResponseStatusException.class,
                () -> authService.validateToken(null));
    }

    @Test
    void validateToken_noBearerPrefix_throwsUnauthorized() {
        assertThrows(ResponseStatusException.class,
                () -> authService.validateToken("just-a-token"));
    }
}
