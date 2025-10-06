package com.hirepath.hirepath_backend.controller.preferredlanguage;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.preferredlanguage.PreferredLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/preferred-language")
public class PreferredLanguageController {
    private final PreferredLanguageService preferredLanguageService;

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> preferredLanguageUpdate(@PathVariable(value = "preferredLanguageGuid") String guid, Principal principal) {
        preferredLanguageService.preferredLanguageUpdate(guid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Preferred language updated successfully"));
    }
}
