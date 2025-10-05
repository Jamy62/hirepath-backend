package com.hirepath.hirepath_backend.service.workexperience;

import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;

public interface WorkExperienceService {
    WorkExperience findByGuid(String guid);
}
