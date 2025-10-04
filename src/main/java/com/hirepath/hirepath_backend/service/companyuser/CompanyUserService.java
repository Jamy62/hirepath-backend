package com.hirepath.hirepath_backend.service.companyuser;

import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;

public interface CompanyUserService {
    CompanyUser findByGuid(String guid);
    void assignCompanyRole(AssignCompanyRoleRequest request, String email);
}
