package com.hirepath.hirepath_backend.model.dto.location;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO {
    private String name;
    private String address;
    private String photo;
    private String guid;
}
