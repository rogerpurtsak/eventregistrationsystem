package com.example.eventregistration.service;

import com.example.eventregistration.dto.RegistrationRequest;
import com.example.eventregistration.entity.EventItem;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.repository.EventItemRepository;
import com.example.eventregistration.repository.RegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private EventItemRepository eventItemRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void registerForEvent_eventNotFound_throwsNotFound() {
        when(eventItemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> registrationService.registerForEvent(1L, buildRequest("38001011234")));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void registerForEvent_eventFull_throwsConflict() {
        EventItem event = buildEvent(10, 10);
        when(eventItemRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.countByEventItemId(1L)).thenReturn(10L);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> registrationService.registerForEvent(1L, buildRequest("38001011234")));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void registerForEvent_duplicatePersonalCode_throwsConflict() {
        EventItem event = buildEvent(10, 10);
        when(eventItemRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.countByEventItemId(1L)).thenReturn(1L);
        when(registrationRepository.existsByEventItemIdAndPersonalCode(1L, "38001011234")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> registrationService.registerForEvent(1L, buildRequest("38001011234")));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void registerForEvent_valid_savesRegistration() {
        EventItem event = buildEvent(10, 10);
        when(eventItemRepository.findById(1L)).thenReturn(Optional.of(event));
        when(registrationRepository.countByEventItemId(1L)).thenReturn(0L);
        when(registrationRepository.existsByEventItemIdAndPersonalCode(1L, "38001011234")).thenReturn(false);

        assertDoesNotThrow(() -> registrationService.registerForEvent(1L, buildRequest("38001011234")));

        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    void registerForEvent_sameCodeDifferentEvent_succeeds() {
        EventItem event2 = buildEvent(10, 10);
        when(eventItemRepository.findById(2L)).thenReturn(Optional.of(event2));
        when(registrationRepository.countByEventItemId(2L)).thenReturn(0L);
        when(registrationRepository.existsByEventItemIdAndPersonalCode(2L, "38001011234")).thenReturn(false);

        assertDoesNotThrow(() -> registrationService.registerForEvent(2L, buildRequest("38001011234")));

        verify(registrationRepository).save(any(Registration.class));
    }

    private RegistrationRequest buildRequest(String personalCode) {
        RegistrationRequest request = new RegistrationRequest();
        request.firstName = "Jaan";
        request.lastName = "Tamm";
        request.personalCode = personalCode;
        return request;
    }

    private EventItem buildEvent(int maxParticipants, int id) {
        EventItem event = new EventItem();
        event.setId((long) id);
        event.setTitle("Test Event");
        event.setMaxParticipants(maxParticipants);
        return event;
    }
}
