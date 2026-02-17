package com.hirepath.hirepath_backend.model.request.jobtype;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class JobTypeCreateRequest {

    private String name;
    private String description;
}
