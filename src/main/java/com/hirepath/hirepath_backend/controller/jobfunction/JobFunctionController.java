package com.hirepath.hirepath_backend.controller.jobfunction;

import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListDTO;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.jobfunction.JobFunctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job-function")
public class JobFunctionController {

    private final JobFunctionService jobFunctionService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionCreate(@Valid @RequestBody JobFunctionCreateRequest request, Principal principal) {
        jobFunctionService.jobFunctionCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job function created successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('SYSTEM', 'COMPANY')")
    public ResponseEntity<ResponseFormat> jobFunctionList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<JobFunctionListDTO> response = jobFunctionService.jobFunctionList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Job function list retrieved successfully"));
    }

    @PutMapping("/update/admin/{jobFunctionGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionUpdate(@PathVariable String jobFunctionGuid, @Valid @RequestBody JobFunctionUpdateRequest request, Principal principal) {
        jobFunctionService.jobFunctionUpdate(jobFunctionGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job function updated successfully"));
    }

    @DeleteMapping("/delete/admin/{jobFunctionGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionDelete(
            @PathVariable(value = "jobFunctionGuid") String jobFunctionGuid,
            Principal principal) {
        jobFunctionService.jobFunctionDelete(jobFunctionGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job function deleted successfully"));
    }
}
