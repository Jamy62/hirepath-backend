package com.hirepath.hirepath_backend.controller.companyuserposition;

import com.hirepath.hirepath_backend.model.dto.companyuserposition.CompanyUserPositionListDTO;
import com.hirepath.hirepath_backend.model.request.companyuserposition.CompanyUserPositionListRequest;
import com.hirepath.hirepath_backend.model.request.companyuserposition.PositionListRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.companyuserposition.CompanyUserPositionService;
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
@RequestMapping("/v1/company-user-position")
public class CompanyUserPositionController {

    private final CompanyUserPositionService companyUserPositionService;

    @PostMapping("/assign/{companyUserGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> companyUserPositionsAssign(@PathVariable String companyUserGuid, @Valid @RequestBody PositionListRequest request, Principal principal, Authentication authentication) {
        companyUserPositionService.companyUserPositionsAssign(companyUserGuid, request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "User positions assigned successfully"));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> companyUserPositionsDelete(@Valid @RequestBody CompanyUserPositionListRequest request, Principal principal, Authentication authentication) {
        companyUserPositionService.companyUserPositionsDelete(request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "User positions removed successfully"));
    }

    @GetMapping("/list/{companyUserGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> companyUserPositionList(@PathVariable String companyUserGuid, Authentication authentication) {
        List<CompanyUserPositionListDTO> positions = companyUserPositionService.companyUserPositionList(companyUserGuid, AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(positions, "User positions fetched successfully"));
    }
}
