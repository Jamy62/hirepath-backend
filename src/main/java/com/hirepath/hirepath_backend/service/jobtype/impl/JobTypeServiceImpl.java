package com.hirepath.hirepath_backend.service.jobtype.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.jobtype.JobTypeListDTO;
import com.hirepath.hirepath_backend.model.dto.jobtype.JobTypeListProjection;
import com.hirepath.hirepath_backend.model.entity.jobtype.JobType;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.jobtype.JobTypeUpdateRequest;
import com.hirepath.hirepath_backend.repository.jobtype.JobTypeRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.jobtype.JobTypeService;
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
public class JobTypeServiceImpl implements JobTypeService {

    private final JobTypeRepository jobTypeRepository;
    private final UserRepository userRepository;

    @Override
    public JobType findByGuid(String guid) {
        try {
            return jobTypeRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job type not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobTypeCreate(JobTypeCreateRequest request, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            JobType jobType = JobType.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            jobTypeRepository.save(jobType);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<JobTypeListDTO> jobTypeList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<JobTypeListProjection> jobTypeListProjections = jobTypeRepository.findAllJobTypesAdminPanel(searchName, orderBy, first, max);

                return jobTypeListProjections.stream()
                        .map(p -> JobTypeListDTO.builder()
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
    public void jobTypeUpdate(String jobTypeGuid, JobTypeUpdateRequest request, String email) {
        try {
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            JobType jobType = findByGuid(jobTypeGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                jobType.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                jobType.setDescription(request.getDescription());
            }

            jobType.setUpdatedAt(ZonedDateTime.now());
            jobType.setUpdatedBy(admin.getId());

            jobTypeRepository.save(jobType);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobTypeDelete(String jobTypeGuid, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            JobType jobType = findByGuid(jobTypeGuid);

            jobType.setIsDeleted(true);
            jobType.setUpdatedAt(ZonedDateTime.now());
            jobType.setUpdatedBy(admin.getId());

            jobTypeRepository.save(jobType);
        } catch (Exception e) {
            throw e;
        }
    }
}
