package com.hirepath.hirepath_backend.controller.companyuser;

import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.companyuser.CompanyUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company-user")
public class CompanyUserController {
    private final CompanyUserService companyUserService;

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> assignCompanyRole(@Valid @RequestBody AssignCompanyRoleRequest request,
                                                            Principal principal) {
        companyUserService.assignCompanyRole(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Company role assigned successfully"));
    }
}
