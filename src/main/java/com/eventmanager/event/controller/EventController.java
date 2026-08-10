package com.eventmanager.event.controller;

import com.eventmanager.event.dto.request.CreateEventRequest;
import com.eventmanager.event.dto.request.EventFilterRequest;
import com.eventmanager.event.dto.request.UpdateEventRequest;
import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.eventmanager.event.dto.request.EventFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Void> create(
            @Valid @RequestBody CreateEventRequest request
    ) {
        eventService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<EventResponse>> findAll(
            EventFilterRequest filter,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                eventService.findAll(filter, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        return ResponseEntity.ok(
                eventService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        eventService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publish(
            @PathVariable Long id
    ){
       return ResponseEntity.ok(
               eventService.publish(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<EventResponse> cancel(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(eventService.cancel(id));
    }

    @PostMapping("/{id}/close-registration")
    public ResponseEntity<EventResponse> closeRegistration(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                eventService.closeRegistration(id)
        );
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<EventResponse> complete(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                eventService.complete(id)
        );
    }
}