package com.eventmanager.event.service;


import com.eventmanager.category.entity.Category;
import com.eventmanager.category.repository.CategoryRepository;
import com.eventmanager.common.exception.BusinessException;
import com.eventmanager.common.exception.ForbiddenException;
import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.event.dto.request.CreateEventRequest;
import com.eventmanager.event.dto.request.UpdateEventRequest;
import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.event.entity.Event;
import com.eventmanager.event.enums.EventStatus;
import com.eventmanager.event.mapper.EventMapper;
import com.eventmanager.event.repository.EventRepository;
import com.eventmanager.location.entity.Location;
import com.eventmanager.location.repository.LocationRepository;
import com.eventmanager.notification.KafkaProducer;
import com.eventmanager.security.CustomUserDetails;
import com.eventmanager.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.eventmanager.event.dto.request.EventFilterRequest;
import com.eventmanager.event.specification.EventSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final KafkaProducer kafkaProducer;

    public void create(CreateEventRequest request){


        Event event = new Event();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartAt(request.getStartAt());
        event.setEndAt(request.getEndAt());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setCapacity(request.getCapacity());

        event.setStatus(EventStatus.DRAFT);
        event.setOrganizer(getCurrentUser());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        event.setCategory(category);

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Location not found"));

        event.setLocation(location);

        eventRepository.save(event);
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

    public Page<EventResponse> findAll(
            EventFilterRequest filter,
            Pageable pageable
    ) {

        return eventRepository
                .findAll(
                        EventSpecification.filter(filter),
                        pageable
                )
                .map(eventMapper::toResponse);
    }


    @Cacheable(value = "events", key = "#id")
    public EventResponse findById(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        return eventMapper.toResponse(event);
    }

    @CacheEvict(value = "events", key = "#id")
    public EventResponse update(Long id, UpdateEventRequest request){
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if(!event.getOrganizer().getId().equals(currentUser.getId())){
            throw new ForbiddenException(
                    "Only the event organizer can update this event"
            );
        }

        if(event.getStatus() != EventStatus.DRAFT){
            throw new BusinessException(
                    "Only DRAFT events can be updated"
            );
        }
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartAt(request.getStartAt());
        event.setEndAt(request.getEndAt());
        event.setRegistrationDeadline(request.getRegistrationDeadline());
        event.setCapacity(request.getCapacity());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        event.setCategory(category);

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Location not found"));

        event.setLocation(location);

        return eventMapper.toResponse(eventRepository.save(event));
    }

    @CacheEvict(value = "events", key = "#id")
    public void delete(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the event organizer can delete this event"
            );
        }

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new BusinessException(
                    "Only draft events can be deleted"
            );
        }

        eventRepository.delete(event);
    }

    @CacheEvict(value = "events", key = "#id")
    public EventResponse publish(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the event organizer can publish this event"
            );
        }

        if (event.getStatus() != EventStatus.DRAFT) {
            throw new BusinessException(
                    "Only DRAFT events can be published"
            );
        }

        event.setStatus(EventStatus.PUBLISHED);

        Event savedEvent = eventRepository.save(event);

        kafkaProducer.sendEventPublished(savedEvent.getId());

        return eventMapper.toResponse(savedEvent);
    }

    @CacheEvict(value = "events", key = "#id")
    public EventResponse cancel(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the event organizer can cancel this event"
            );
        }

        if (event.getStatus() != EventStatus.DRAFT &&
                event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessException(
                    "Only DRAFT or PUBLISHED events can be cancelled"
            );
        }

        event.setStatus(EventStatus.CANCELLED);

        return eventMapper.toResponse(eventRepository.save(event));
    }


    @CacheEvict(value = "events", key = "#id")
    public EventResponse closeRegistration(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the event organizer can close registration"
            );
        }

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessException(
                    "Only PUBLISHED events can close registration"
            );
        }

        event.setStatus(EventStatus.REGISTRATION_CLOSED);

        return eventMapper.toResponse(eventRepository.save(event));
    }

    @CacheEvict(value = "events", key = "#id")
    public EventResponse complete(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));

        User currentUser = getCurrentUser();

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new ForbiddenException(
                    "Only the event organizer can complete this event"
            );
        }

        if (event.getStatus() != EventStatus.REGISTRATION_CLOSED) {
            throw new BusinessException(
                    "Only events with closed registration can be completed"
            );
        }

        event.setStatus(EventStatus.COMPLETED);

        return eventMapper.toResponse(eventRepository.save(event));
    }
}
