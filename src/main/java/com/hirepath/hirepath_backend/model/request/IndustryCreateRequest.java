package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IndustryCreateRequest {
    private String name;
    private String description;
}
