package com.example.eventregistration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventregistration.dto.EventRequest;
import com.example.eventregistration.dto.EventResponse;
import com.example.eventregistration.entity.EventItem;
import com.example.eventregistration.repository.RegistrationRepository;
import com.example.eventregistration.repository.EventItemRepository;

@Service
public class EventService {

    @Autowired
    private EventItemRepository eventItemRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
        
    public List<EventResponse> getAllEvents() {
        return eventItemRepository.findAll().stream().map(eventItem -> {
            EventResponse response = new EventResponse();
            response.id = eventItem.getId();
            response.title = eventItem.getTitle();
            response.eventTime = eventItem.getEventTime();
            response.maxParticipants = eventItem.getMaxParticipants();
            response.registeredCount = (int) registrationRepository.countByEventItemId(eventItem.getId());
            response.availableSpots = response.maxParticipants - response.registeredCount;
            return response;
        }).toList();

    }

    public EventResponse createEvent(EventRequest request) {
        EventItem eventItem = new EventItem();
        eventItem.setTitle(request.title);
        eventItem.setEventTime(request.eventTime);
        eventItem.setMaxParticipants(request.maxParticipants);

        eventItem = eventItemRepository.save(eventItem);

        EventResponse response = new EventResponse();
        response.id = eventItem.getId();
        response.title = eventItem.getTitle();
        response.eventTime = eventItem.getEventTime();
        response.maxParticipants = eventItem.getMaxParticipants();
        response.registeredCount = (int) registrationRepository.countByEventItemId(eventItem.getId());
        response.availableSpots = response.maxParticipants - response.registeredCount;

        return response;
    }
}
