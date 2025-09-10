package com.hirepath.hirepath_backend.service.company;

import com.hirepath.hirepath_backend.model.request.company.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface CompanyService {

    ResponseFormat companyRegister(CompanyRegisterRequest request);

    ResponseFormat companyList(String searchName, String orderBy, int first, int max);

    ResponseFormat companyUpdate(String companyGuid, CompanyUpdateRequest request, String adminEmail);

    ResponseFormat companyDelete(String companyGuid, String adminEmail);
}
