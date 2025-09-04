package com.hirepath.hirepath_backend.service.plan;

import com.hirepath.hirepath_backend.model.request.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.PlanUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface PlanService {

    ResponseFormat planCreate(PlanCreateRequest request, String adminEmail);

    ResponseFormat planList(String searchName, String orderBy, int first, int max);

    ResponseFormat planUpdate(PlanUpdateRequest request, String adminEmail);

    ResponseFormat planDelete(String planGuid, String adminEmail);
}

