package com.hirepath.hirepath_backend.controller.auth;

import com.hirepath.hirepath_backend.model.request.CompanySwitchRequest;
import com.hirepath.hirepath_backend.model.request.LoginRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseFormat> login(@RequestBody LoginRequest request) {
        ResponseFormat responseFormat = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(responseFormat);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Principal principal ) {
        authService.logout(principal.getName());
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/company-access")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ResponseFormat> companyAccess(@RequestBody CompanySwitchRequest request,
                                                        Principal principal) {
        ResponseFormat responseFormat = authService.companyAccess(request.getCompanyGuid(), principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

}
