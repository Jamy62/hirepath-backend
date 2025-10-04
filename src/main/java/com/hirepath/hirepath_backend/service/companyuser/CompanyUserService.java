package com.hirepath.hirepath_backend.service.companyuser;

import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;

public interface CompanyUserService {
    void assignCompanyRole(AssignCompanyRoleRequest request, String email);
}
