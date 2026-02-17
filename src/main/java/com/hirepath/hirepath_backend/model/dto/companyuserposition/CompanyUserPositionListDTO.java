package com.hirepath.hirepath_backend.model.dto.companyuserposition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyUserPositionListDTO {
    private String name;
    private String guid;
    private String positionGuid;
}
