package com.eventmanager.event.dto.request;


import com.eventmanager.event.enums.EventStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFilterRequest {

    private String title;

    private Long categoryId;

    private Long locationId;

    private EventStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTo;
}
