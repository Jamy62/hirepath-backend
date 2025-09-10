package com.hirepath.hirepath_backend.model.request.jobtype;

import lombok.Data;

@Data
public class JobTypeUpdateRequest {
    private String name;
    private String description;
}
