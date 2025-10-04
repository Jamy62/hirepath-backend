package com.hirepath.hirepath_backend.service.jobtype;

import com.hirepath.hirepath_backend.model.dto.jobtype.JobTypeListDTO;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeUpdateRequest;

import java.util.List;

public interface JobTypeService {

    void jobTypeCreate(JobTypeCreateRequest request, String adminEmail);

    List<JobTypeListDTO> jobTypeList(String searchName, String orderBy, int first, int max);

    void jobTypeUpdate(String jobTypeGuid, JobTypeUpdateRequest request, String email);

    void jobTypeDelete(String jobTypeGuid, String adminEmail);
}
