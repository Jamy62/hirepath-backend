package com.hirepath.hirepath_backend.model.request.township;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class TownshipCreateRequest {
    private String name;
    private String provinceGuid;
}
