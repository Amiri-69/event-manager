package com.eventmanager.event.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotNull
    @Future
    private LocalDateTime startAt;

    @NotNull
    @Future
    private LocalDateTime endAt;

    @NotNull
    @Future
    private LocalDateTime registrationDeadline;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long locationId;
}