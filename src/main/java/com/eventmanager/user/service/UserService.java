package com.eventmanager.user.service;

import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.event.entity.Event;
import com.eventmanager.event.mapper.EventMapper;
import com.eventmanager.event.repository.EventRepository;
import com.eventmanager.registration.dto.response.RegistrationResponse;
import com.eventmanager.registration.mapper.RegistrationMapper;
import com.eventmanager.registration.repository.RegistrationRepository;
import com.eventmanager.security.CustomUserDetails;
import com.eventmanager.user.dto.response.UserResponse;
import com.eventmanager.user.entity.User;
import com.eventmanager.user.mapper.UserMapper;
import com.eventmanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;


    public UserResponse getCurrentUser() {

        User user = getCurrentUserEntity();

        return userMapper.toResponse(user);
    }


    public List<EventResponse> getMyEvents() {

        User currentUser = getCurrentUserEntity();

        return eventRepository.findAllByOrganizerId(currentUser.getId())
                .stream()
                .map(eventMapper::toResponse)
                .toList();
    }


    public List<RegistrationResponse> getMyRegistrations() {

        User currentUser = getCurrentUserEntity();

        return registrationRepository
                .findAllByUserId(currentUser.getId())
                .stream()
                .map(registrationMapper::toResponse)
                .toList();
    }


    private User getCurrentUserEntity() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }
}