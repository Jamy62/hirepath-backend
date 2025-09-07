package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IndustryUpdateRequest {

    @NotBlank(message = "IndustryGuid must not be blank")
    private String industryGuid;
    private String name;
    private String description;
}
