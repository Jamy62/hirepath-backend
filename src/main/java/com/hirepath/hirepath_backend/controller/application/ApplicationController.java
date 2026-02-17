package com.hirepath.hirepath_backend.controller.application;

import com.hirepath.hirepath_backend.model.dto.application.ApplicationDetailDTO;
import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListDTO;
import com.hirepath.hirepath_backend.model.request.application.ApplicationAcceptRequest;
import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
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
@RequestMapping("/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> apply(@Valid @RequestBody ApplicationCreateRequest request, Principal principal) {
        applicationService.apply(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Application submitted successfully"));
    }

    @PostMapping("/accept/{applicationGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> acceptApplication(@PathVariable String applicationGuid, @RequestBody ApplicationAcceptRequest request, Principal principal, Authentication authentication) {
        applicationService.acceptApplication(applicationGuid, request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Application accepted successfully"));
    }

    @PostMapping("/reject/{applicationGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> rejectApplication(@PathVariable String applicationGuid, Principal principal, Authentication authentication) {
        applicationService.rejectApplication(applicationGuid, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Application rejected successfully"));
    }

    @GetMapping("/list/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> userApplications(Principal principal) {
        List<UserApplicationListDTO> applications = applicationService.userApplications(principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(applications, "User applications fetched successfully"));
    }

    @GetMapping("/list/company")
    @PreAuthorize("hasAnyRole('COMPANY')")
    public ResponseEntity<ResponseFormat> companyApplications(Authentication authentication) {
        List<CompanyApplicationListDTO> applications = applicationService.companyApplications(AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(applications, "Company applications fetched successfully"));
    }

    @GetMapping("/detail/{applicationGuid}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY')")
    public ResponseEntity<ResponseFormat> applicationDetail(@PathVariable String applicationGuid, Principal principal) {
        ApplicationDetailDTO applicationDetail = applicationService.applicationDetail(applicationGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(applicationDetail, "Application detail fetched successfully"));
    }
}
