package com.hirepath.hirepath_backend.service.position;

import com.hirepath.hirepath_backend.model.entity.position.Position;

public interface PositionService {
    Position findByGuid(String guid);
}
