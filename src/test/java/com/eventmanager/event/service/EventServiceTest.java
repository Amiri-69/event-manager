package com.eventmanager.event.service;

import com.eventmanager.category.entity.Category;
import com.eventmanager.common.exception.BusinessException;
import com.eventmanager.common.exception.ForbiddenException;
import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.event.dto.request.UpdateEventRequest;
import com.eventmanager.event.dto.response.EventResponse;
import com.eventmanager.event.entity.Event;
import com.eventmanager.event.enums.EventStatus;
import com.eventmanager.event.mapper.EventMapper;
import com.eventmanager.event.repository.EventRepository;
import com.eventmanager.location.entity.Location;
import com.eventmanager.security.CustomUserDetails;
import com.eventmanager.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private com.eventmanager.category.repository.CategoryRepository categoryRepository;

    @Mock
    private com.eventmanager.location.repository.LocationRepository locationRepository;

    @InjectMocks
    private EventService eventService;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;


    private void mockCurrentUser(User user) {

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(userDetails.getUser())
                .thenReturn(user);
    }


    @Test
    void findById_shouldReturnEvent() {

        Event event = new Event();
        event.setId(1L);

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(eventMapper.toResponse(event))
                .thenReturn(response);


        EventResponse result = eventService.findById(1L);


        assertEquals(1L, result.getId());

        verify(eventRepository).findById(1L);
        verify(eventMapper).toResponse(event);
    }


    @Test
    void findById_shouldThrowExceptionWhenEventNotFound() {

        when(eventRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> eventService.findById(1L)
        );


        verify(eventRepository).findById(1L);
        verify(eventMapper, never()).toResponse(any());
    }



    @Test
    void update_shouldUpdateEvent_whenOrganizerAndDraft() {

        // GIVEN
        Long eventId = 1L;

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setId(eventId);
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        Category category = new Category();
        category.setId(2L);

        Location location = new Location();
        location.setId(3L);

        UpdateEventRequest request = new UpdateEventRequest();
        request.setTitle("Updated event");
        request.setCategoryId(2L);
        request.setLocationId(3L);

        EventResponse response = new EventResponse();

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(event));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(category));

        when(locationRepository.findById(3L))
                .thenReturn(Optional.of(location));

        when(eventRepository.save(event))
                .thenReturn(event);

        when(eventMapper.toResponse(event))
                .thenReturn(response);


        // WHEN
        EventResponse result =
                eventService.update(eventId, request);


        // THEN
        assertEquals("Updated event", event.getTitle());
        assertEquals(category, event.getCategory());
        assertEquals(location, event.getLocation());

        assertSame(response, result);

        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(event);
    }


    @Test
    void update_shouldThrowForbidden_whenUserIsNotOrganizer() {

        User organizer = new User();
        organizer.setId(10L);

        User anotherUser = new User();
        anotherUser.setId(20L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        UpdateEventRequest request = new UpdateEventRequest();

        when(userDetails.getUser())
                .thenReturn(anotherUser);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));


        assertThrows(
                ForbiddenException.class,
                () -> eventService.update(1L, request)
        );

        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowBusinessException_whenEventIsNotDraft() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        UpdateEventRequest request = new UpdateEventRequest();

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));


        assertThrows(
                BusinessException.class,
                () -> eventService.update(1L, request)
        );

        verify(categoryRepository, never()).findById(any());
        verify(locationRepository, never()).findById(any());
        verify(eventRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowException_whenCategoryNotFound() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        UpdateEventRequest request = new UpdateEventRequest();
        request.setCategoryId(99L);
        request.setLocationId(1L);

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> eventService.update(1L, request)
        );

        verify(locationRepository, never()).findById(any());
        verify(eventRepository, never()).save(any());
    }


    @Test
    void update_shouldThrowException_whenLocationNotFound() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        Category category = new Category();

        UpdateEventRequest request = new UpdateEventRequest();
        request.setCategoryId(2L);
        request.setLocationId(99L);

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(category));

        when(locationRepository.findById(99L))
                .thenReturn(Optional.empty());


        assertThrows(
                ResourceNotFoundException.class,
                () -> eventService.update(1L, request)
        );

        verify(eventRepository, never()).save(any());
    }


    @Test
    void delete_shouldDeleteEvent_whenOrganizerAndDraft() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));


        eventService.delete(1L);


        verify(eventRepository).delete(event);
    }


    @Test
    void delete_shouldThrowForbidden_whenUserIsNotOrganizer() {

        User organizer = new User();
        organizer.setId(10L);

        User anotherUser = new User();
        anotherUser.setId(20L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        when(userDetails.getUser())
                .thenReturn(anotherUser);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));


        assertThrows(
                ForbiddenException.class,
                () -> eventService.delete(1L)
        );


        verify(eventRepository, never()).delete(any(Event.class));
    }


    @Test
    void delete_shouldThrowBusinessException_whenEventIsNotDraft() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        when(userDetails.getUser())
                .thenReturn(organizer);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));


        assertThrows(
                BusinessException.class,
                () -> eventService.delete(1L)
        );


        verify(eventRepository, never()).delete(any(Event.class));
    }


    @Test
    void publish_shouldPublishEvent_whenOrganizerAndDraft() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        EventResponse response = new EventResponse();

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventRepository.save(event))
                .thenReturn(event);
        when(eventMapper.toResponse(event))
                .thenReturn(response);

        EventResponse result = eventService.publish(1L);

        assertEquals(EventStatus.PUBLISHED, event.getStatus());
        assertSame(response, result);

        verify(eventRepository).save(event);
    }


    @Test
    void publish_shouldThrowForbidden_whenUserIsNotOrganizer() {

        User organizer = new User();
        organizer.setId(10L);

        User anotherUser = new User();
        anotherUser.setId(20L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        when(userDetails.getUser()).thenReturn(anotherUser);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        assertThrows(
                ForbiddenException.class,
                () -> eventService.publish(1L)
        );

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void publish_shouldThrowBusinessException_whenEventIsNotDraft() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        assertThrows(
                BusinessException.class,
                () -> eventService.publish(1L)
        );

        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    void cancel_shouldCancelDraftEvent() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        EventResponse response = new EventResponse();

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventRepository.save(event))
                .thenReturn(event);
        when(eventMapper.toResponse(event))
                .thenReturn(response);

        EventResponse result = eventService.cancel(1L);

        assertEquals(EventStatus.CANCELLED, event.getStatus());
        assertSame(response, result);

        verify(eventRepository).save(event);
    }


    @Test
    void cancel_shouldCancelPublishedEvent() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        EventResponse response = new EventResponse();

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventRepository.save(event))
                .thenReturn(event);
        when(eventMapper.toResponse(event))
                .thenReturn(response);

        eventService.cancel(1L);

        assertEquals(EventStatus.CANCELLED, event.getStatus());

        verify(eventRepository).save(event);
    }


    @Test
    void cancel_shouldThrowBusinessException_whenEventCannotBeCancelled() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.COMPLETED);

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        assertThrows(
                BusinessException.class,
                () -> eventService.cancel(1L)
        );

        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    void closeRegistration_shouldCloseRegistration_whenEventIsPublished() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        EventResponse response = new EventResponse();

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventRepository.save(event))
                .thenReturn(event);
        when(eventMapper.toResponse(event))
                .thenReturn(response);

        EventResponse result =
                eventService.closeRegistration(1L);

        assertEquals(
                EventStatus.REGISTRATION_CLOSED,
                event.getStatus()
        );

        assertSame(response, result);

        verify(eventRepository).save(event);
    }


    @Test
    void closeRegistration_shouldThrowBusinessException_whenEventIsNotPublished() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.DRAFT);

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        assertThrows(
                BusinessException.class,
                () -> eventService.closeRegistration(1L)
        );

        verify(eventRepository, never()).save(any(Event.class));
    }


    @Test
    void complete_shouldCompleteEvent_whenRegistrationIsClosed() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.REGISTRATION_CLOSED);

        EventResponse response = new EventResponse();

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));
        when(eventRepository.save(event))
                .thenReturn(event);
        when(eventMapper.toResponse(event))
                .thenReturn(response);

        EventResponse result =
                eventService.complete(1L);

        assertEquals(
                EventStatus.COMPLETED,
                event.getStatus()
        );

        assertSame(response, result);

        verify(eventRepository).save(event);
    }


    @Test
    void complete_shouldThrowBusinessException_whenRegistrationIsNotClosed() {

        User organizer = new User();
        organizer.setId(10L);

        mockCurrentUser(organizer);

        Event event = new Event();
        event.setOrganizer(organizer);
        event.setStatus(EventStatus.PUBLISHED);

        when(userDetails.getUser()).thenReturn(organizer);
        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        assertThrows(
                BusinessException.class,
                () -> eventService.complete(1L)
        );

        verify(eventRepository, never()).save(any(Event.class));
    }
}