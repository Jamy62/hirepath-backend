package com.hirepath.hirepath_backend.service.jobfunction;

import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListDTO;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionUpdateRequest;

import java.util.List;

public interface JobFunctionService {

    JobFunction findByGuid(String guid);

    void jobFunctionCreate(JobFunctionCreateRequest request, String adminEmail);

    List<JobFunctionListDTO> jobFunctionList(String searchName, String orderBy, int first, int max);

    void jobFunctionUpdate(String jobFunctionGuid, JobFunctionUpdateRequest request, String email);

    void jobFunctionDelete(String jobFunctionGuid, String adminEmail);
}
