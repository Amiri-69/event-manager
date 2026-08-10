package com.eventmanager.user.controller;

import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.registration.dto.response.RegistrationResponse;
import com.eventmanager.user.dto.response.UserResponse;
import com.eventmanager.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/me/events")
    public ResponseEntity<List<EventResponse>> getMyEvents() {
        return ResponseEntity.ok(userService.getMyEvents());
    }

    @GetMapping("/me/registrations")
    public ResponseEntity<List<RegistrationResponse>> getMyRegistrations() {
        return ResponseEntity.ok(userService.getMyRegistrations());
    }
}