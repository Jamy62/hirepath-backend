package com.hirepath.hirepath_backend.service.job;

import com.hirepath.hirepath_backend.model.entity.job.Job;

public interface JobService {
    Job findByGuid(String guid);
}
