package com.hirepath.hirepath_backend.model.request;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class ProvinceCreateRequest {
    private String name;
}
