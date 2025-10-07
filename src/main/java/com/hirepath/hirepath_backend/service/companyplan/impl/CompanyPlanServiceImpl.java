package com.hirepath.hirepath_backend.service.companyplan.impl;

import com.hirepath.hirepath_backend.model.dto.companyplan.ActivePlanDTO;
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
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;
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
            Optional<CompanyPlan> previousPlanOpt = companyPlanRepository.findByCompanyAndIsDeletedFalse(company);
            ZonedDateTime endDate = ZonedDateTime.now().plusDays(plan.getDurationDays());

            if (!company.equals(paymentMethod.getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            if (previousPlanOpt.isPresent()) {
                CompanyPlan previousPlan = previousPlanOpt.get();
                previousPlan.setIsActive(false);
                previousPlan.setIsDeleted(true);
                previousPlan.setUpdatedAt(ZonedDateTime.now());
                previousPlan.setUpdatedBy(company.getId());
                companyPlanRepository.save(previousPlan);
            }

            CompanyPlan companyPlan = CompanyPlan.builder()
                    .company(company)
                    .plan(plan)
                    .paymentMethod(paymentMethod)
                    .startDate(ZonedDateTime.now())
                    .endDate(endDate)
                    .isActive(true)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(company.getId())
                    .build();
            companyPlanRepository.save(companyPlan);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ActivePlanDTO getActivePlan(String companyGuid) {
        Company company = companyService.findByGuid(companyGuid);
        Optional<CompanyPlan> companyPlanOpt = companyPlanRepository.findByCompanyAndIsDeletedFalse(company);

        if (companyPlanOpt.isPresent()) {
            CompanyPlan companyPlan = companyPlanOpt.get();
            return ActivePlanDTO.builder()
                    .plan(companyPlan.getPlan().getName())
                    .startDate(companyPlan.getStartDate())
                    .endDate(companyPlan.getEndDate())
                    .isActive(companyPlan.getIsActive())
                    .build();
        }

        return null;
    }
}
