package com.hirepath.hirepath_backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanUpdateRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private String duration;
    private Integer durationDays;
    private String features;
}
