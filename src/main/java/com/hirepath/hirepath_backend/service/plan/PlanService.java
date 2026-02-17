package com.hirepath.hirepath_backend.service.plan;

import com.hirepath.hirepath_backend.model.dto.plan.PlanListDTO;
import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import com.hirepath.hirepath_backend.model.request.plan.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.plan.PlanUpdateRequest;

import java.util.List;

public interface PlanService {

    Plan findByGuid(String guid);

    void planCreate(PlanCreateRequest request, String adminEmail);

    List<PlanListDTO> planList(String searchName, String orderBy, int first, int max);

    void planUpdate(String planGuid, PlanUpdateRequest request, String adminEmail);

    void planDelete(String planGuid, String adminEmail);
}
