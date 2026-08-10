package com.eventmanager.registration.controller;

import com.eventmanager.registration.dto.response.RegistrationResponse;
import com.eventmanager.registration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<Void> register(
            @PathVariable Long eventId
    ) {
        registrationService.register(eventId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> unregister(
            @PathVariable Long eventId
    ) {
        registrationService.unregister(eventId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RegistrationResponse>> findAll(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(
                registrationService.findAllByEvent(eventId)
        );
    }
}