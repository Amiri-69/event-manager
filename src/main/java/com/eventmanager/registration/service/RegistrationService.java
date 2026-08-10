package com.eventmanager.registration.service;

import com.eventmanager.common.exception.BusinessException;
import com.eventmanager.common.exception.ForbiddenException;
import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.event.entity.Event;
import com.eventmanager.event.enums.EventStatus;
import com.eventmanager.event.repository.EventRepository;
import com.eventmanager.registration.dto.response.RegistrationResponse;
import com.eventmanager.registration.entity.Registration;
import com.eventmanager.registration.mapper.RegistrationMapper;
import com.eventmanager.registration.repository.RegistrationRepository;
import com.eventmanager.security.CustomUserDetails;
import com.eventmanager.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final RegistrationMapper registrationMapper;

    public void register(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessException(
                    "Registration is available only for published events"
            );
        }

        if (LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new BusinessException(
                    "Registration deadline has passed"
            );
        }

        long registrationsCount =
                registrationRepository.countByEventId(eventId);

        if (registrationsCount >= event.getCapacity()) {
            throw new BusinessException(
                    "Event is full"
            );
        }

        if (registrationRepository.existsByEventIdAndUserId(
                eventId,
                currentUser.getId()
        )) {
            throw new BusinessException(
                    "User is already registered for this event"
            );
        }

        Registration registration = new Registration();

        registration.setEvent(event);
        registration.setUser(currentUser);
        registration.setRegisteredAt(LocalDateTime.now());

        registrationRepository.save(registration);
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

    public void unregister(Long eventId) {

        Registration registration =
                registrationRepository.findByEventIdAndUserId(
                        eventId,
                        getCurrentUser().getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Registration not found"
                        ));

        registrationRepository.delete(registration);
    }

    public List<RegistrationResponse> findAllByEvent(Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Event not found");
        }

        return registrationRepository.findAllByEventId(eventId)
                .stream()
                .map(registrationMapper::toResponse)
                .toList();
    }
}