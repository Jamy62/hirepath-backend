package com.hirepath.hirepath_backend.controller.industry;

import com.hirepath.hirepath_backend.model.request.industry.IndustryCreateRequest;
import com.hirepath.hirepath_backend.model.request.industry.IndustryUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/industry")
public class IndustryController {

    private final IndustryService industryService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> industryCreate(@Valid @RequestBody IndustryCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = industryService.industryCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> industryList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = industryService.industryList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{industryGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> industryUpdate(@PathVariable String industryGuid, @Valid @RequestBody IndustryUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = industryService.industryUpdate(industryGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{industryGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> industryDelete(
            @PathVariable(value = "industryGuid") String industryGuid,
            Principal principal) {
        ResponseFormat responseFormat = industryService.industryDelete(industryGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
