package com.hirepath.hirepath_backend.service.jobfunction.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListDTO;
import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListProjection;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.jobfunction.JobFunctionRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.jobfunction.JobFunctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobFunctionServiceImpl implements JobFunctionService {

    private final JobFunctionRepository jobFunctionRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseFormat jobFunctionCreate(JobFunctionCreateRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        JobFunction jobFunction = JobFunction.builder()
                .name(request.getName())
                .description(request.getDescription())
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(admin.getId())
                .build();

        jobFunctionRepository.save(jobFunction);

        return ResponseFormat.createSuccessResponse(null, "Job function created successfully");
    }

    @Override
    public ResponseFormat jobFunctionList(String searchName, String orderBy, int first, int max) {
        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            // This method will need to be created in JobFunctionRepository
            List<JobFunctionListProjection> jobFunctionListProjections = jobFunctionRepository.findAllJobFunctionsAdminPanel(searchName, orderBy, first, max);

            List<JobFunctionListDTO> jobFunctions = jobFunctionListProjections.stream()
                    .map(p -> JobFunctionListDTO.builder()
                            .name(p.getName())
                            .description(p.getDescription())
                            .guid(p.getGuid())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();

            return ResponseFormat.createSuccessResponse(jobFunctions, "Job function list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat jobFunctionUpdate(String jobFunctionGuid, JobFunctionUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        // This method will need to be created in JobFunctionRepository
        JobFunction jobFunction = jobFunctionRepository.findByGuid(jobFunctionGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job function not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            jobFunction.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            jobFunction.setDescription(request.getDescription());
        }

        jobFunction.setUpdatedAt(ZonedDateTime.now());
        jobFunction.setUpdatedBy(admin.getId());

        jobFunctionRepository.save(jobFunction);

        return ResponseFormat.createSuccessResponse(null, "Job function updated successfully");
    }

    @Override
    public ResponseFormat jobFunctionDelete(String jobFunctionGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        // This method will need to be created in JobFunctionRepository
        JobFunction jobFunction = jobFunctionRepository.findByGuid(jobFunctionGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job function not found"));

        jobFunction.setIsDeleted(true);
        jobFunction.setUpdatedAt(ZonedDateTime.now());
        jobFunction.setUpdatedBy(admin.getId());

        jobFunctionRepository.save(jobFunction);

        return ResponseFormat.createSuccessResponse(null, "Job function deleted successfully");
    }
}
