package com.eventmanager.registration.repository;

import com.eventmanager.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    long countByEventId(Long eventId);
    Optional<Registration> findByEventIdAndUserId(Long eventId, Long userId);
    List<Registration> findAllByUserId(Long userId);
    List<Registration> findAllByEventId(Long eventId);
}
