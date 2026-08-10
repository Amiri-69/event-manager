package com.eventmanager.registration.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RegistrationResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDateTime registeredAt;
    private Long eventId;
    private String eventTitle;
}