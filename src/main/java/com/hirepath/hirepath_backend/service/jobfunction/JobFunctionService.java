package com.hirepath.hirepath_backend.service.jobfunction;

import com.hirepath.hirepath_backend.model.request.JobFunctionCreateRequest;
import com.hirepath.hirepath_backend.model.request.JobFunctionUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface JobFunctionService {

    ResponseFormat jobFunctionCreate(JobFunctionCreateRequest request, String adminEmail);

    ResponseFormat jobFunctionList(String searchName, String orderBy, int first, int max);

    ResponseFormat jobFunctionUpdate(String jobFunctionGuid, JobFunctionUpdateRequest request, String email);

    ResponseFormat jobFunctionDelete(String jobFunctionGuid, String adminEmail);
}
