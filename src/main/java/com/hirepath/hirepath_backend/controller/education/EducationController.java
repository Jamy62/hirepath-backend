package com.hirepath.hirepath_backend.controller.education;

import com.hirepath.hirepath_backend.model.request.education.EducationCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.education.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/education")
public class EducationController {
    private final EducationService educationService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> educationCreate(@Valid @RequestBody EducationCreateRequest request, Principal principal) {
        educationService.educationCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Education created successfully"));
    }

    @PostMapping("/delete/{educationGuid}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> educationDelete(@PathVariable(value = "educationGuid") String educationGuid,
                                                          Principal principal) {
        educationService.educationDelete(educationGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Education deleted successfully"));
    }
}
