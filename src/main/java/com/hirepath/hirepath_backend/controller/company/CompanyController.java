package com.hirepath.hirepath_backend.controller.company;

import com.hirepath.hirepath_backend.model.request.company.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyResponseRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/company")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<ResponseFormat> companyRegister(@Valid @RequestBody CompanyRegisterRequest request,
                                                          Principal principal) {
        ResponseFormat responseFormat = companyService.companyRegister(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/verify/{companyGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> companyVerify(@PathVariable String companyGuid,
                                                        @Valid @RequestBody CompanyVerifyRequest request,
                                                        Principal principal,
                                                        Authentication authentication) {
        AuthenticationUtil.isPageMember(authentication.getDetails(), companyGuid);
        ResponseFormat responseFormat = companyService.companyVerify(companyGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/verify/response/admin/{companyGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> verifyResponse(@PathVariable String companyGuid,
                                                         @Valid @RequestBody CompanyVerifyResponseRequest request,
                                                         Principal principal) {
        ResponseFormat responseFormat = companyService.verifyResponse(companyGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> companyList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = companyService.companyList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/verify-list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> companyVerifyList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = companyService.companyVerifyList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{companyGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> companyUpdate(@PathVariable String companyGuid, @Valid @RequestBody CompanyUpdateRequest request,
                                                        Principal principal,
                                                        Authentication authentication) {
        AuthenticationUtil.isPageMember(authentication.getDetails(), companyGuid);
        ResponseFormat responseFormat = companyService.companyUpdate(companyGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{companyGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> companyDelete(
            @PathVariable(value = "companyGuid") String companyGuid,
            Principal principal) {
        ResponseFormat responseFormat = companyService.companyDelete(companyGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
