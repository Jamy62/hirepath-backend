package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Plan guid must not be blank")
    private String planGuid;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationInDays;
    private String features;
}

