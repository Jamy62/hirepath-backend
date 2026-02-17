package com.hirepath.hirepath_backend.model.request.companyuser;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class ReassignCompanyRoleRequest {
    @NotBlank(message = "New role GUID is required")
    private String newRoleGuid;
}
