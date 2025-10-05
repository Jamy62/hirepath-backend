package com.hirepath.hirepath_backend.service.companyplan.impl;

import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.repository.companyplan.CompanyPlanRepository;
import com.hirepath.hirepath_backend.service.companyplan.CompanyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CompanyPlanServiceImpl implements CompanyPlanService {

    private final CompanyPlanRepository companyPlanRepository;

    @Override
    public CompanyPlan findByGuid(String guid) {
        try {
            return companyPlanRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CompanyPlan not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
