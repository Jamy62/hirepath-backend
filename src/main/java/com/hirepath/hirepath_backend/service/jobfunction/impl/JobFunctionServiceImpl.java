package com.hirepath.hirepath_backend.service.jobfunction.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListDTO;
import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListProjection;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobfunction.JobFunctionUpdateRequest;
import com.hirepath.hirepath_backend.repository.jobfunction.JobFunctionRepository;
import com.hirepath.hirepath_backend.service.jobfunction.JobFunctionService;
import com.hirepath.hirepath_backend.service.user.UserService;
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
    private final UserService userService;

    @Override
    public JobFunction findByGuid(String guid) {
        try {
            return jobFunctionRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job function not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobFunctionCreate(JobFunctionCreateRequest request, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            JobFunction jobFunction = JobFunction.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            jobFunctionRepository.save(jobFunction);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<JobFunctionListDTO> jobFunctionList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<JobFunctionListProjection> jobFunctionListProjections = jobFunctionRepository.findAllJobFunctionsAdminPanel(searchName, orderBy, first, max);

                return jobFunctionListProjections.stream()
                        .map(p -> JobFunctionListDTO.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobFunctionUpdate(String jobFunctionGuid, JobFunctionUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            JobFunction jobFunction = findByGuid(jobFunctionGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                jobFunction.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                jobFunction.setDescription(request.getDescription());
            }

            jobFunction.setUpdatedAt(ZonedDateTime.now());
            jobFunction.setUpdatedBy(admin.getId());

            jobFunctionRepository.save(jobFunction);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobFunctionDelete(String jobFunctionGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            JobFunction jobFunction = findByGuid(jobFunctionGuid);

            jobFunction.setIsDeleted(true);
            jobFunction.setUpdatedAt(ZonedDateTime.now());
            jobFunction.setUpdatedBy(admin.getId());

            jobFunctionRepository.save(jobFunction);
        } catch (Exception e) {
            throw e;
        }
    }
}
