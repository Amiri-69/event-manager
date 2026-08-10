package com.eventmanager.location.service;

import com.eventmanager.common.exception.ResourceNotFoundException;
import com.eventmanager.location.dto.request.CreateLocationRequest;
import com.eventmanager.location.dto.response.LocationResponse;
import com.eventmanager.location.entity.Location;
import com.eventmanager.location.mapper.LocationMapper;
import com.eventmanager.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationResponse create(CreateLocationRequest request) {

        Location location = locationMapper.toEntity(request);

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toResponse(savedLocation);
    }

    public List<LocationResponse> findAll() {

        return locationRepository.findAll()
                .stream()
                .map(locationMapper::toResponse)
                .toList();
    }

    public LocationResponse update(
            Long id,
            CreateLocationRequest request
    ) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found"
                        ));

        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setCity(request.getCity());
        location.setCapacity(request.getCapacity());

        Location savedLocation =
                locationRepository.save(location);

        return locationMapper.toResponse(savedLocation);
    }

    public void delete(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found"
                        ));

        locationRepository.delete(location);
    }
}