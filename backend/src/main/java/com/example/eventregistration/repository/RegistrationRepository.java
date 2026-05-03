package com.example.eventregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventregistration.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    public long countByEventItemId(Long eventItemId);

    public boolean existsByEventItemIdAndPersonalCode(Long eventItemId, String personalCode);

}
