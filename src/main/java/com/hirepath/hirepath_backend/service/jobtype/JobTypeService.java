package com.hirepath.hirepath_backend.service.jobtype;

import com.hirepath.hirepath_backend.model.request.JobTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.JobTypeUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface JobTypeService {

    ResponseFormat jobTypeCreate(JobTypeCreateRequest request, String adminEmail);

    ResponseFormat jobTypeList(String searchName, String orderBy, int first, int max);

    ResponseFormat jobTypeUpdate(String jobTypeGuid, JobTypeUpdateRequest request, String email);

    ResponseFormat jobTypeDelete(String jobTypeGuid, String adminEmail);
}
