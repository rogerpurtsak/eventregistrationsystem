package com.example.eventregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventregistration.entity.EventItem;

public interface EventItemRepository extends JpaRepository<EventItem, Long> {

}
