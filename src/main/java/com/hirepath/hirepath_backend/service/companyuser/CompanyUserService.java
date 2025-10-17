package com.hirepath.hirepath_backend.service.companyuser;

import com.hirepath.hirepath_backend.model.dto.companyuser.CompanyUserListDTO;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;

import java.util.List;

public interface CompanyUserService {
    CompanyUser findByGuid(String guid);
    void assignCompanyRole(AssignCompanyRoleRequest request, String email);
    List<CompanyUserListDTO> employeeList(String companyGuid);
}
