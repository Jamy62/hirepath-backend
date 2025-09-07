package com.hirepath.hirepath_backend.model.request;

import lombok.Data;

@Data
public class TownshipUpdateRequest {
    private String name;
    private String provinceGuid;
}
