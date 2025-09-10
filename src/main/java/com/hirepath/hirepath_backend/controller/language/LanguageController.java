package com.hirepath.hirepath_backend.controller.language;

import com.hirepath.hirepath_backend.model.request.language.LanguageCreateRequest;
import com.hirepath.hirepath_backend.model.request.language.LanguageUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.language.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/language")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> createLanguage(@Valid @RequestBody LanguageCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = languageService.languageCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> listLanguages(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = languageService.languageList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{languageGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> updateLanguage(@PathVariable String languageGuid, @Valid @RequestBody LanguageUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = languageService.languageUpdate(languageGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{languageGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> deleteLanguage(
            @PathVariable(value = "languageGuid") String languageGuid,
            Principal principal) {
        ResponseFormat responseFormat = languageService.languageDelete(languageGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
