package com.hirepath.hirepath_backend.service.application;

import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListDTO;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;

import java.util.List;

public interface ApplicationService {
    Application findByGuid(String guid);
    void apply(ApplicationCreateRequest request, String email);
    List<UserApplicationListDTO> userApplications(String email);
    List<CompanyApplicationListDTO> companyApplications(String companyGuid);
}
