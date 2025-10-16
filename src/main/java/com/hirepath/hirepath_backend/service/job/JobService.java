package com.hirepath.hirepath_backend.service.job;

import com.hirepath.hirepath_backend.model.dto.job.JobDetailDTO;
import com.hirepath.hirepath_backend.model.dto.job.JobListDTO;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.request.job.JobCreateRequest;

import java.util.List;

public interface JobService {
    Job findByGuid(String guid);
    void jobCreate(JobCreateRequest request, String email, String companyGuid);
    List<JobListDTO> jobList(String searchTitle, String companyGuid, String provinceGuid, String townshipGuid,
                             String jobTypeGuid, String experienceLevelGuid, String industryGuid, Double salary, String orderBy, int first, int max);
    JobDetailDTO jobDetail(String jobGuid);
}
