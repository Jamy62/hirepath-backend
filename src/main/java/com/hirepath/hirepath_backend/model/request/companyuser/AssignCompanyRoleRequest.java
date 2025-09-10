package com.hirepath.hirepath_backend.model.request.companyuser;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class AssignCompanyRoleRequest {
    private String companyGuid;
    private String userGuid;
    private String roleGuid;
}
