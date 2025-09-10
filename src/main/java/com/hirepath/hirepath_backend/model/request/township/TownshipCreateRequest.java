package com.hirepath.hirepath_backend.model.request.township;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@NotBlank
@Data
public class TownshipCreateRequest {
    private String name;
    private String provinceGuid;
}
