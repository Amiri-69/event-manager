package com.eventmanager.event.mapper;

import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.event.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {

        EventResponse response = new EventResponse();

        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setStartAt(event.getStartAt());
        response.setEndAt(event.getEndAt());
        response.setRegistrationDeadline(event.getRegistrationDeadline());
        response.setCapacity(event.getCapacity());
        response.setStatus(event.getStatus());
        response.setOrganizerId(event.getOrganizer().getId());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());
        response.setCategoryId(event.getCategory().getId());
        response.setLocationId(event.getLocation().getId());

        return response;
    }
}