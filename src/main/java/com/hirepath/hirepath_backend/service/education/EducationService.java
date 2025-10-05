package com.hirepath.hirepath_backend.service.education;

import com.hirepath.hirepath_backend.model.entity.education.Education;

public interface EducationService {
    Education findByGuid(String guid);
}
