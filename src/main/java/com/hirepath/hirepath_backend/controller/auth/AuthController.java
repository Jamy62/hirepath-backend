package com.hirepath.hirepath_backend.controller.auth;

import com.hirepath.hirepath_backend.model.request.companyuser.CompanySwitchRequest;
import com.hirepath.hirepath_backend.model.request.user.LoginRequest;
import com.hirepath.hirepath_backend.model.response.LoginResponse;
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
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Login success"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Principal principal) {
        authService.logout(principal.getName());
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/company-access")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> companyAccess(@RequestBody CompanySwitchRequest request,
                                                        Principal principal) {
        LoginResponse response = authService.companyAccess(request.getCompanyGuid(), principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Successfully switched to company"));
    }

}
