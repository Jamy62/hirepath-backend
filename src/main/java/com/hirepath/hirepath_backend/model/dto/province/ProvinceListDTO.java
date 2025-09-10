package com.hirepath.hirepath_backend.model.dto.province;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ProvinceListDTO {
    private String name;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
