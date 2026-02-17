package com.hirepath.hirepath_backend.controller.preferredindustry;

import com.hirepath.hirepath_backend.model.request.preferredindustry.PreferredIndustryCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.preferredindustry.PreferredIndustryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/preferred-industry")
public class PreferredIndustryController {
    private final PreferredIndustryService preferredIndustryService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> preferredIndustryCreate(@Valid @RequestBody PreferredIndustryCreateRequest request, Principal principal) {
        preferredIndustryService.preferredIndustryCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Preferred industry created successfully"));
    }

    @PostMapping("/delete/{preferredIndustryGuid}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> preferredIndustryDelete(@PathVariable(value = "preferredIndustryGuid") String preferredIndustryGuid,
                                                                  Principal principal) {
        preferredIndustryService.preferredIndustryDelete(preferredIndustryGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Preferred industry deleted successfully"));
    }
}
