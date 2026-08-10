package com.eventmanager.location.controller;

import com.eventmanager.location.dto.request.CreateLocationRequest;
import com.eventmanager.location.dto.response.LocationResponse;
import com.eventmanager.location.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponse> create(
            @Valid @RequestBody CreateLocationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(locationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> findAll() {

        return ResponseEntity.ok(
                locationService.findAll()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateLocationRequest request
    ) {
        return ResponseEntity.ok(
                locationService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        locationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}