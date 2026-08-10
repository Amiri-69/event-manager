package com.eventmanager.registration.mapper;

import com.eventmanager.registration.dto.response.RegistrationResponse;
import com.eventmanager.registration.entity.Registration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper {

    public RegistrationResponse toResponse(Registration registration) {

        RegistrationResponse response = new RegistrationResponse();

        response.setUserId(registration.getUser().getId());
        response.setFirstName(registration.getUser().getFirstname());
        response.setLastName(registration.getUser().getLastname());
        response.setRegisteredAt(registration.getRegisteredAt());
        response.setEventId(registration.getEvent().getId());
        response.setEventTitle(registration.getEvent().getTitle());

        return response;
    }
}