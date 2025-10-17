package com.hirepath.hirepath_backend.model.dto.position;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PositionListDTO {
    private String name;
    private String description;
    private String guid;
}
