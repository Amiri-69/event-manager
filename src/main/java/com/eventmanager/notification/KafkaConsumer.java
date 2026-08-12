package com.eventmanager.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "event.published",
            groupId = "notification-group"
    )
    public void consume(String eventId) {

        System.out.println(
                "Received event.published: " + eventId
        );
    }
}