package com.hirepath.hirepath_backend.model.request.jobfunction;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class JobFunctionCreateRequest {
    private String name;
    private String description;
}
