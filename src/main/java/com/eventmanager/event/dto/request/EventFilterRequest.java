package com.eventmanager.event.dto.request;


import com.eventmanager.event.enums.EventStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFilterRequest {

    private String title;

    private Long categoryId;

    private Long locationId;

    private EventStatus status;

    private LocalDateTime startFrom;

    private LocalDateTime endTo;
}
