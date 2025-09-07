package com.hirepath.hirepath_backend.model.request;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class JobFunctionCreateRequest {
    private String name;
    private String description;
}
