package com.eventmanager.location.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLocationRequest {

    @NotBlank
    @Size(min = 2, max = 150)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(min = 2, max = 100)
    private String city;

    @NotNull
    @Min(1)
    private Integer capacity;
}