package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class PlanCreateRequest {

    @NotBlank(message = "Plan name must not be blank")
    private String name;

    private String description;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    @NotNull(message = "Duration must not be null")
    private String duration;

    @NotNull(message = "DurationDays must not be null")
    private Integer durationDays;

    private String features;
}

