package com.hirepath.hirepath_backend.service.company;

import com.hirepath.hirepath_backend.model.request.company.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyResponseRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface CompanyService {

    ResponseFormat companyRegister(CompanyRegisterRequest request, String email);

    ResponseFormat companyVerify(String companyGuid, CompanyVerifyRequest request, String email);

    ResponseFormat verifyResponse(String companyGuid, CompanyVerifyResponseRequest request, String email);

    ResponseFormat companyList(String searchName, String orderBy, int first, int max);

    ResponseFormat companyVerifyList(String searchName, String orderBy, int first, int max);

    ResponseFormat companyUpdate(String companyGuid, CompanyUpdateRequest request, String adminEmail);

    ResponseFormat companyDelete(String companyGuid, String adminEmail);
}
