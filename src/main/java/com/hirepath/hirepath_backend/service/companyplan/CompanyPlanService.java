package com.hirepath.hirepath_backend.service.companyplan;

import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;

public interface CompanyPlanService {
    CompanyPlan findByGuid(String guid);
}
