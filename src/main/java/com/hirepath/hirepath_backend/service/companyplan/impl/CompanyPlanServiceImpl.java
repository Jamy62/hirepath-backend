package com.hirepath.hirepath_backend.service.companyplan.impl;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import com.hirepath.hirepath_backend.model.request.companyplan.PurchasePlanRequest;
import com.hirepath.hirepath_backend.repository.companyplan.CompanyPlanRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.companyplan.CompanyPlanService;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
import com.hirepath.hirepath_backend.service.plan.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyPlanServiceImpl implements CompanyPlanService {

    private final CompanyPlanRepository companyPlanRepository;
    private final CompanyService companyService;
    private final PlanService planService;
    private final PaymentMethodService paymentMethodService;

    @Override
    public CompanyPlan findByGuid(String guid) {
        try {
            return companyPlanRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CompanyPlan not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void purchasePlan(PurchasePlanRequest request, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            Plan plan = planService.findByGuid(request.getPlanGuid());
            PaymentMethod paymentMethod = paymentMethodService.findByGuid(request.getPaymentMethodGuid());

            if (!company.equals(paymentMethod.getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            CompanyPlan companyPlan = CompanyPlan.builder()
                    .company(company)
                    .plan(plan)
                    .paymentMethod(paymentMethod)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .updatedAt(ZonedDateTime.now())
                    .updatedBy(company.getId())
                    .build();
            companyPlanRepository.save(companyPlan);
        } catch (Exception e) {
            throw e;
        }
    }
}
