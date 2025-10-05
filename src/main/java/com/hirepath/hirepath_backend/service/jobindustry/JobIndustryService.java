package com.hirepath.hirepath_backend.service.jobindustry;

import com.hirepath.hirepath_backend.model.entity.jobindustry.JobIndustry;

public interface JobIndustryService {
    JobIndustry findByGuid(String guid);
}
