package com.hirepath.hirepath_backend.service.education;

import com.hirepath.hirepath_backend.model.entity.education.Education;
import com.hirepath.hirepath_backend.model.request.education.EducationCreateRequest;

public interface EducationService {
    Education findByGuid(String guid);
    void educationCreate(EducationCreateRequest request, String email);
    void educationDelete(String educationGuid, String email);
}
