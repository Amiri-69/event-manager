package com.eventmanager.location.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationResponse {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Integer capacity;
}