package com.hirepath.hirepath_backend.controller.jobfunction;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job-function")
public class JobFunctionController {

    private final JobFunctionService jobFunctionService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionCreate(@Valid @RequestBody JobFunctionCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = jobFunctionService.jobFunctionCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = jobFunctionService.jobFunctionList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{jobFunctionGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionUpdate(@PathVariable String jobFunctionGuid, @Valid @RequestBody JobFunctionUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = jobFunctionService.jobFunctionUpdate(jobFunctionGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{jobFunctionGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobFunctionDelete(
            @PathVariable(value = "jobFunctionGuid") String jobFunctionGuid,
            Principal principal) {
        ResponseFormat responseFormat = jobFunctionService.jobFunctionDelete(jobFunctionGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
