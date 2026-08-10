package com.eventmanager.admin.service;

import com.eventmanager.admin.dto.response.AdminStatisticsResponse;
import com.eventmanager.admin.dto.response.AdminUserResponse;
import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.event.repository.EventRepository;
import com.eventmanager.registration.repository.RegistrationRepository;
import com.eventmanager.user.entity.User;
import com.eventmanager.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public List<AdminUserResponse> getUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void blockUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setBlocked(true);

        userRepository.save(user);
    }

    public void unblockUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setBlocked(false);

        userRepository.save(user);
    }

    public AdminStatisticsResponse getStatistics() {

        return new AdminStatisticsResponse(
                userRepository.count(),
                eventRepository.count(),
                registrationRepository.count()
        );
    }

    private AdminUserResponse toResponse(User user) {

        AdminUserResponse response = new AdminUserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstname());
        response.setLastName(user.getLastname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setBlocked(user.isBlocked());

        return response;
    }
}