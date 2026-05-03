package com.example.eventregistration.service;

import com.example.eventregistration.dto.EventRequest;
import com.example.eventregistration.dto.EventResponse;
import com.example.eventregistration.entity.EventItem;
import com.example.eventregistration.repository.EventItemRepository;
import com.example.eventregistration.repository.RegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventItemRepository eventItemRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void getAllEvents_noEvents_returnsEmptyList() {
        when(eventItemRepository.findAll()).thenReturn(List.of());

        List<EventResponse> result = eventService.getAllEvents();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllEvents_withEvents_returnsMappedResponses() {
        EventItem event = buildEvent(1L, "Jooksuvõistlus", 100);
        when(eventItemRepository.findAll()).thenReturn(List.of(event));
        when(registrationRepository.countByEventItemId(1L)).thenReturn(3L);

        List<EventResponse> result = eventService.getAllEvents();

        assertEquals(1, result.size());
        EventResponse response = result.get(0);
        assertEquals(1L, response.id);
        assertEquals("Jooksuvõistlus", response.title);
        assertEquals(100, response.maxParticipants);
        assertEquals(3, response.registeredCount);
        assertEquals(97, response.availableSpots);
    }

    @Test
    void createEvent_savesAndReturnsMappedResponse() {
        EventRequest request = new EventRequest();
        request.title = "Konverents";
        request.eventTime = LocalDateTime.of(2026, 9, 1, 12, 0);
        request.maxParticipants = 50;

        EventItem saved = buildEvent(1L, "Konverents", 50);
        saved.setEventTime(request.eventTime);
        when(eventItemRepository.save(any(EventItem.class))).thenReturn(saved);
        when(registrationRepository.countByEventItemId(1L)).thenReturn(0L);

        EventResponse response = eventService.createEvent(request);

        assertEquals("Konverents", response.title);
        assertEquals(50, response.maxParticipants);
        assertEquals(0, response.registeredCount);
        assertEquals(50, response.availableSpots);
        verify(eventItemRepository).save(any(EventItem.class));
    }

    private EventItem buildEvent(Long id, String title, int maxParticipants) {
        EventItem event = new EventItem();
        event.setId(id);
        event.setTitle(title);
        event.setEventTime(LocalDateTime.of(2026, 8, 1, 10, 0));
        event.setMaxParticipants(maxParticipants);
        return event;
    }
}
