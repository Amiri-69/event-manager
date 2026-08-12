package com.eventmanager.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyEventPublished(Long eventId) {

        System.out.println(
                "Notification: Event " + eventId + " was published"
        );
    }
}