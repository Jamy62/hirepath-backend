package com.hirepath.hirepath_backend.service.plan;

import com.hirepath.hirepath_backend.model.request.plan.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.plan.PlanUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface PlanService {

    ResponseFormat planCreate(PlanCreateRequest request, String adminEmail);

    ResponseFormat planList(String searchName, String orderBy, int first, int max);

    ResponseFormat planUpdate(String planGuid, PlanUpdateRequest request, String adminEmail);

    ResponseFormat planDelete(String planGuid, String adminEmail);
}
