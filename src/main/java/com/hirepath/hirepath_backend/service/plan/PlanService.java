package com.hirepath.hirepath_backend.service.plan;

import com.hirepath.hirepath_backend.model.request.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.PlanUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface PlanService {

    ResponseFormat createPlan(PlanCreateRequest request, String adminEmail);

    ResponseFormat listPlans(String searchName, String orderBy, int first, int max);

    ResponseFormat updatePlan(PlanUpdateRequest request, String adminEmail);

    ResponseFormat deletePlan(String planGuid, String adminEmail);
}

