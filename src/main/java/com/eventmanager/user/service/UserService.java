package com.eventmanager.user.service;


import com.eventmanager.user.dto.request.RegisterUserRequest;
import com.eventmanager.user.entity.User;
import com.eventmanager.user.enums.Role;
import com.eventmanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


}
