package com.hirepath.hirepath_backend.model.dto.companyuser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyUserPositionsDTO {
    private String name;
    private String guid;
}
