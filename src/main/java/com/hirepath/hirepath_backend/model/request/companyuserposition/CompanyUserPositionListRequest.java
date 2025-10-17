package com.hirepath.hirepath_backend.model.request.companyuserposition;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CompanyUserPositionListRequest {
    @NotEmpty
    private List<String> companyUserPositionGuids;
}
