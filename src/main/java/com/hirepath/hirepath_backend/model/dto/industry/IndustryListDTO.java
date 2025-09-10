package com.hirepath.hirepath_backend.model.dto.industry;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class IndustryListDTO {
    private String name;
    private String description;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
