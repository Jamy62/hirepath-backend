package com.hirepath.hirepath_backend.controller.application;

import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
}
