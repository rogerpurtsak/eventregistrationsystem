package com.example.eventregistration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eventregistration.dto.RegistrationRequest;
import com.example.eventregistration.entity.EventItem;
import com.example.eventregistration.entity.Registration;
import com.example.eventregistration.repository.EventItemRepository;
import com.example.eventregistration.repository.RegistrationRepository;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventItemRepository eventItemRepository;

    public void registerForEvent(Long eventItemId, RegistrationRequest request) {

        if (eventItemId == null) {
            throw new RuntimeException("Event ID cannot be null.");
        }

        EventItem eventItem = eventItemRepository.findById(eventItemId)
        .orElseThrow(() -> new RuntimeException("Event not found."));

        if (registrationRepository.countByEventItemId(eventItemId) >= eventItem.getMaxParticipants()) {
            throw new RuntimeException("No more spots available for this event.");
        }

        if (registrationRepository.existsByEventItemIdAndPersonalCode(eventItemId, request.personalCode)) {
            throw new RuntimeException("You have already registered for this event.");
        }

        Registration registration = new Registration();
        registration.setFirstName(request.firstName);
        registration.setLastName(request.lastName);
        registration.setPersonalCode(request.personalCode);
        registration.setEventItem(eventItem);

        registrationRepository.save(registration);

        
    }
}
