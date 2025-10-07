package com.hirepath.hirepath_backend.controller.companyplan;

import com.hirepath.hirepath_backend.model.request.companyplan.PurchasePlanRequest;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.companyplan.CompanyPlanService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company-plan")
public class CompanyPlanController {
    private final CompanyPlanService companyPlanService;

    @PostMapping("/purchase")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER'")
    public ResponseEntity<ResponseFormat> purchasePlan(@Valid @RequestBody PurchasePlanRequest request,
                                                            Authentication authentication) {
        companyPlanService.purchasePlan(request, AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Plan purchased successfully"));
    }
}
