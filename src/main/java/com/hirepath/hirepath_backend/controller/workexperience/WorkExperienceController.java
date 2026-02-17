package com.hirepath.hirepath_backend.controller.workexperience;

import com.hirepath.hirepath_backend.model.request.workexperience.WorkExperienceCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.workexperience.WorkExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/work-experience")
public class WorkExperienceController {
    private final WorkExperienceService workExperienceService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> workExperienceCreate(@Valid @RequestBody WorkExperienceCreateRequest request, Principal principal) {
        workExperienceService.workExperienceCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Work experience created successfully"));
    }

    @PostMapping("/delete/{workExperienceGuid}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> workExperienceDelete(@PathVariable(value = "workExperienceGuid") String workExperienceGuid,
                                                             Principal principal) {
        workExperienceService.workExperienceDelete(workExperienceGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Work experience deleted successfully"));
    }
}
