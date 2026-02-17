package com.hirepath.hirepath_backend.controller.userlanuage;

import com.hirepath.hirepath_backend.model.request.userLanguage.UserLanguageCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.userlanguage.UserLanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user-language")
public class UserLanguageController {
    private final UserLanguageService userLanguageService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> userLanguageCreate(@Valid @RequestBody UserLanguageCreateRequest request, Principal principal) {
        userLanguageService.userLanguageCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "User language created successfully"));
    }

    @PostMapping("/delete/{userLanguageGuid}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> userLanguageDelete(@PathVariable(value = "userLanguageGuid") String userLanguageGuid,
                                                             Principal principal) {
        userLanguageService.userLanguageDelete(userLanguageGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "User language deleted successfully"));
    }
}
