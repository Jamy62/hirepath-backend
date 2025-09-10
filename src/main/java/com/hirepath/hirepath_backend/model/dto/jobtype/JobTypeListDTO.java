package com.hirepath.hirepath_backend.model.dto.jobtype;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class JobTypeListDTO {
    private String name;
    private String description;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
