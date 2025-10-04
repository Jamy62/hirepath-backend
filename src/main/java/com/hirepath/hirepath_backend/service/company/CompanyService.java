package com.hirepath.hirepath_backend.service.company;

import com.hirepath.hirepath_backend.model.dto.company.CompanyListDTO;
import com.hirepath.hirepath_backend.model.request.company.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyResponseRequest;

import java.util.List;

public interface CompanyService {

    void companyRegister(CompanyRegisterRequest request, String email);

    void companyVerify(String companyGuid, CompanyVerifyRequest request, String email);

    void verifyResponse(String companyGuid, CompanyVerifyResponseRequest request, String email);

    List<CompanyListDTO> companyList(String searchName, String orderBy, int first, int max);

    List<CompanyListDTO> companyVerifyList(String searchName, String orderBy, int first, int max);

    void companyUpdate(String companyGuid, CompanyUpdateRequest request, String adminEmail);

    void companyDelete(String companyGuid, String adminEmail);
}
