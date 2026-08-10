package com.eventmanager.location.mapper;

import com.eventmanager.location.dto.request.CreateLocationRequest;
import com.eventmanager.location.dto.response.LocationResponse;
import com.eventmanager.location.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location toEntity(CreateLocationRequest request) {

        Location location = new Location();

        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setCity(request.getCity());
        location.setCapacity(request.getCapacity());

        return location;
    }

    public LocationResponse toResponse(Location location) {

        LocationResponse response = new LocationResponse();

        response.setId(location.getId());
        response.setName(location.getName());
        response.setAddress(location.getAddress());
        response.setCity(location.getCity());
        response.setCapacity(location.getCapacity());

        return response;
    }
}