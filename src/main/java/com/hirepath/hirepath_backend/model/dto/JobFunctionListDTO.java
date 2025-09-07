package com.hirepath.hirepath_backend.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class JobFunctionListDTO {
    private String name;
    private String description;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
