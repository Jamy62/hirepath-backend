package com.hirepath.hirepath_backend.service.application;

import com.hirepath.hirepath_backend.model.dto.application.ApplicationDetailDTO;
import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListDTO;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.application.ApplicationAcceptRequest;
import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;

import java.util.List;

public interface ApplicationService {
    Application findByGuid(String guid);
    List<Application> findAllByUserAndCompanyAndStatus(User user, Company company, String status);
    void apply(ApplicationCreateRequest request, String email);
    void acceptApplication(String applicationGuid, ApplicationAcceptRequest request, String email, String companyGuid);
    void rejectApplication(String applicationGuid, String email, String companyGuid);
    List<UserApplicationListDTO> userApplications(String email);
    List<CompanyApplicationListDTO> companyApplications(String companyGuid);
    ApplicationDetailDTO applicationDetail(String applicationGuid, String email);
}
