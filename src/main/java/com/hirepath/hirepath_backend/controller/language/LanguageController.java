package com.hirepath.hirepath_backend.controller.language;

import com.hirepath.hirepath_backend.model.dto.language.LanguageListDTO;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/language")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> languageCreate(@Valid @RequestBody LanguageCreateRequest request, Principal principal) {
        languageService.languageCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Language created successfully"));
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> languageList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<LanguageListDTO> response = languageService.languageList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Language list retrieved successfully"));
    }

    @PutMapping("/update/admin/{languageGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> languageUpdate(@PathVariable String languageGuid, @Valid @RequestBody LanguageUpdateRequest request, Principal principal) {
        languageService.languageUpdate(languageGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Language updated successfully"));
    }

    @DeleteMapping("/delete/admin/{languageGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> languageDelete(
            @PathVariable(value = "languageGuid") String languageGuid,
            Principal principal) {
        languageService.languageDelete(languageGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Language deleted successfully"));
    }
}
