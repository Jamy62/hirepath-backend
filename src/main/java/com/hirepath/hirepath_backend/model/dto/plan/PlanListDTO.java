package com.hirepath.hirepath_backend.model.dto.plan;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class PlanListDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private String duration;
    private Integer durationDays;
    private String features;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}

