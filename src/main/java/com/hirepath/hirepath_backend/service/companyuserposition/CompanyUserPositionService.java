package com.hirepath.hirepath_backend.service.companyuserposition;

import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;

public interface CompanyUserPositionService {
    CompanyUserPosition findByGuid(String guid);
}
