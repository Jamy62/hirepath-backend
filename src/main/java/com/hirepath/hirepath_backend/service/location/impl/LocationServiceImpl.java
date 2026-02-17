package com.hirepath.hirepath_backend.service.location.impl;

import com.hirepath.hirepath_backend.model.entity.location.Location;
import com.hirepath.hirepath_backend.repository.location.LocationRepository;
import com.hirepath.hirepath_backend.service.location.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location findByGuid(String guid) {
        try {
            return locationRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
