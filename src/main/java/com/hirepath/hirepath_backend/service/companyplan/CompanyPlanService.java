package com.hirepath.hirepath_backend.service.companyplan;

import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.request.companyplan.PurchasePlanRequest;

public interface CompanyPlanService {
    CompanyPlan findByGuid(String guid);
    void purchasePlan(PurchasePlanRequest request, String companyGuid);
}
