package com.hirepath.hirepath_backend.controller.position;

import com.hirepath.hirepath_backend.model.dto.position.PositionListDTO;
import com.hirepath.hirepath_backend.model.request.position.PositionCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.position.PositionService;
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
@RequestMapping("/v1/position")
public class PositionController {

    private final PositionService positionService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> positionCreate(@Valid @RequestBody PositionCreateRequest request, Principal principal, Authentication authentication) {
        positionService.positionCreate(request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Position created successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('COMPANY')")
    public ResponseEntity<ResponseFormat> positionList(Authentication authentication) {
        List<PositionListDTO> positions = positionService.positionList(AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(positions, "Positions fetched successfully"));
    }

    @PostMapping("/delete/{positionGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> positionDelete(@PathVariable String positionGuid, Principal principal, Authentication authentication) {
        positionService.positionDelete(positionGuid, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Position deleted successfully"));
    }
}
