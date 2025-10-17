package com.hirepath.hirepath_backend.service.companyplan;

import com.hirepath.hirepath_backend.model.dto.companyplan.ActivePlanDTO;
import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.request.companyplan.PurchasePlanRequest;

public interface CompanyPlanService {
    CompanyPlan findByGuid(String guid);
    void purchasePlan(PurchasePlanRequest request, String email, String companyGuid);
    ActivePlanDTO getActivePlan(String ownerCompanyGuid);
}
