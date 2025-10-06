package com.hirepath.hirepath_backend.service.workexperience;

import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;
import com.hirepath.hirepath_backend.model.request.workexperience.WorkExperienceCreateRequest;

public interface WorkExperienceService {
    WorkExperience findByGuid(String guid);
    void workExperienceCreate(WorkExperienceCreateRequest request, String email);
    void workExperienceDelete(String workExperienceGuid, String email);
}
