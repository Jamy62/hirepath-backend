package com.hirepath.hirepath_backend.controller.jobtype;

import com.hirepath.hirepath_backend.model.dto.jobtype.JobTypeListDTO;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.jobtype.JobTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job-type")
public class JobTypeController {

    private final JobTypeService jobTypeService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeCreate(@Valid @RequestBody JobTypeCreateRequest request, Principal principal) {
        jobTypeService.jobTypeCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job type created successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('SYSTEM', 'COMPANY')")
    public ResponseEntity<ResponseFormat> jobTypeList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<JobTypeListDTO> response = jobTypeService.jobTypeList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Job type list retrieved successfully"));
    }

    @PutMapping("/update/admin/{jobTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeUpdate(@PathVariable String jobTypeGuid, @Valid @RequestBody JobTypeUpdateRequest request, Principal principal) {
        jobTypeService.jobTypeUpdate(jobTypeGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job type updated successfully"));
    }

    @DeleteMapping("/delete/admin/{jobTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeDelete(
            @PathVariable(value = "jobTypeGuid") String jobTypeGuid,
            Principal principal) {
        jobTypeService.jobTypeDelete(jobTypeGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job type deleted successfully"));
    }
}
