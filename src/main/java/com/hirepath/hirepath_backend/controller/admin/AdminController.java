package com.hirepath.hirepath_backend.controller.admin;

import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.admin.AdminService;
import com.hirepath.hirepath_backend.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "v1/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> adminRegister(@Valid @RequestBody RegisterRequest request) {
        ResponseFormat responseFormat = adminService.adminRegister(request);
        return ResponseEntity.ok(responseFormat);
    }

}
