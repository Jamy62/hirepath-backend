package com.hirepath.hirepath_backend.model.request.experiencelevel;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class ExperienceLevelCreateRequest {
    private String name;
    private String description;
}
