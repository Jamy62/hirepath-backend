package com.hirepath.hirepath_backend.controller.companyuser;

import com.hirepath.hirepath_backend.model.dto.companyuser.CompanyUserListDTO;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.request.companyuser.ReassignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.companyuser.CompanyUserService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company-user")
public class CompanyUserController {
    private final CompanyUserService companyUserService;

    @PostMapping("/assign")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> assignCompanyRole(@Valid @RequestBody AssignCompanyRoleRequest request, Principal principal) {
        companyUserService.assignCompanyRole(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Company role assigned successfully"));
    }

    @PutMapping("/reassign/{companyUserGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<ResponseFormat> reassignCompanyRole(@PathVariable String companyUserGuid,
                                                              @Valid @RequestBody ReassignCompanyRoleRequest request,
                                                              Principal principal,
                                                              Authentication authentication) {
        companyUserService.reassignCompanyRole(companyUserGuid, request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Company role reassigned successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('COMPANY')")
    public ResponseEntity<ResponseFormat> employeeList(Authentication authentication) {
        List<CompanyUserListDTO> employees = companyUserService.employeeList(AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(employees, "Company employees fetched successfully"));
    }

    @PostMapping("/delete/{companyUserGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> employeeDelete(@PathVariable String companyUserGuid, Principal principal, Authentication authentication) {
        companyUserService.employeeDelete(companyUserGuid, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Company employee deleted successfully"));
    }
}
