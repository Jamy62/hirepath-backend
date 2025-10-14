package com.hirepath.hirepath_backend.controller.job;

import com.hirepath.hirepath_backend.model.dto.job.JobListDTO;
import com.hirepath.hirepath_backend.model.request.job.JobCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.job.JobService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/job")
public class JobController {

    private final JobService jobService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER', 'COMPANY_ADMIN')")
    public ResponseEntity<ResponseFormat> jobCreate(@Valid @RequestBody JobCreateRequest request, Principal principal, Authentication authentication) {
        jobService.jobCreate(request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Job created successfully"));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseFormat> jobList(
            @RequestParam(value = "searchTitle", required = false) String searchTitle,
            @RequestParam(value = "companyGuid", required = false) String companyGuid,
            @RequestParam(value = "townshipGuid", required = false) String townshipGuid,
            @RequestParam(value = "jobTypeGuid", required = false) String jobTypeGuid,
            @RequestParam(value = "experienceLevelGuid", required = false) String experienceLevelGuid,
            @RequestParam(value = "industryGuid", required = false) String industryGuid,
            @RequestParam(value = "minSalary", required = false) Double minSalary,
            @RequestParam(value = "maxSalary", required = false) Double maxSalary,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {

        List<JobListDTO> jobList = jobService.jobList(
                searchTitle, companyGuid, townshipGuid, jobTypeGuid, experienceLevelGuid, industryGuid,
                minSalary, maxSalary, orderBy, first, max);

        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(jobList, "Job list fetched successfully"));
    }
}
