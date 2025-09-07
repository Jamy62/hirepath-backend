package com.hirepath.hirepath_backend.model.request;

import lombok.Data;

@Data
public class JobFunctionUpdateRequest {
    private String name;
    private String description;
}
