package com.eventmanager.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendEventPublished(Long eventId) {

        kafkaTemplate.send(
                "event.published",
                eventId.toString()
        );
    }
}