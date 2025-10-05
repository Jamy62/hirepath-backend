package com.hirepath.hirepath_backend.service.position.impl;

import com.hirepath.hirepath_backend.model.entity.position.Position;
import com.hirepath.hirepath_backend.repository.position.PositionRepository;
import com.hirepath.hirepath_backend.service.position.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public Position findByGuid(String guid) {
        try {
            return positionRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
