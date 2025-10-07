package com.hirepath.hirepath_backend.service.job;

import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.request.job.JobCreateRequest;

public interface JobService {
    Job findByGuid(String guid);
    void jobCreate(JobCreateRequest request, String email, String companyGuid);
}
