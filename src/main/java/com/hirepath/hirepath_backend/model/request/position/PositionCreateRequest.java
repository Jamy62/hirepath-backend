package com.hirepath.hirepath_backend.model.request.position;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class PositionCreateRequest {
    private String name;
    private String description;
}
