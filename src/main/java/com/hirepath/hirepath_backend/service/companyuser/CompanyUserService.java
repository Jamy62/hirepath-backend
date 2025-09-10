package com.hirepath.hirepath_backend.service.companyuser;

import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface CompanyUserService {
    ResponseFormat assignCompanyRole(AssignCompanyRoleRequest request, String email);
}
