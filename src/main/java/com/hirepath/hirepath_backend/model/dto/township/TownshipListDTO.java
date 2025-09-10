package com.hirepath.hirepath_backend.model.dto.township;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class TownshipListDTO {
    private String name;
    private String provinceName;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
