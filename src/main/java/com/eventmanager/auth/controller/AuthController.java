package com.eventmanager.auth.controller;


import com.eventmanager.auth.dto.request.LoginRequest;
import com.eventmanager.auth.dto.response.AuthResponse;
import com.eventmanager.auth.service.AuthService;
import com.eventmanager.user.dto.request.RegisterUserRequest;
import com.eventmanager.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterUserRequest request
            ){
        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }
}
