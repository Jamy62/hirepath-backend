package com.hirepath.hirepath_backend.service.position;

import com.hirepath.hirepath_backend.model.dto.position.PositionListDTO;
import com.hirepath.hirepath_backend.model.entity.position.Position;
import com.hirepath.hirepath_backend.model.request.position.PositionCreateRequest;

import java.util.List;

public interface PositionService {
    Position findByGuid(String guid);
    void positionCreate(PositionCreateRequest request, String email, String companyGuid);
    List<PositionListDTO> positionList(String companyGuid);
}
