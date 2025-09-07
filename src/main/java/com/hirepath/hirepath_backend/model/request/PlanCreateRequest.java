package com.hirepath.hirepath_backend.model.request;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.math.BigDecimal;

@AutoNotBlank
@Data
public class PlanCreateRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String duration;
    private Integer durationDays;
    private String features;
}

