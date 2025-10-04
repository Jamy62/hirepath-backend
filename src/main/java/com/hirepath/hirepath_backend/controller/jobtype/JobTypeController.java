package com.hirepath.hirepath_backend.controller.jobtype;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job-type")
public class JobTypeController {

    private final JobTypeService jobTypeService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeCreate(@Valid @RequestBody JobTypeCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = jobTypeService.jobTypeCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = jobTypeService.jobTypeList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{jobTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeUpdate(@PathVariable String jobTypeGuid, @Valid @RequestBody JobTypeUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = jobTypeService.jobTypeUpdate(jobTypeGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{jobTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> jobTypeDelete(
            @PathVariable(value = "jobTypeGuid") String jobTypeGuid,
            Principal principal) {
        ResponseFormat responseFormat = jobTypeService.jobTypeDelete(jobTypeGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
