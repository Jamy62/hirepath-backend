package com.hirepath.hirepath_backend.service.workexperience.impl;

import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;
import com.hirepath.hirepath_backend.model.request.workexperience.WorkExperienceCreateRequest;
import com.hirepath.hirepath_backend.repository.workexperience.WorkExperienceRepository;
import com.hirepath.hirepath_backend.service.user.UserService;
import com.hirepath.hirepath_backend.service.workexperience.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final UserService userService;

    @Override
    public WorkExperience findByGuid(String guid) {
        try {
            return workExperienceRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkExperience not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void workExperienceCreate(WorkExperienceCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            WorkExperience workExperience = WorkExperience.builder()
                    .user(user)
                    .companyName(request.getCompanyName())
                    .position(request.getPosition())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            workExperienceRepository.save(workExperience);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void workExperienceDelete(String workExperienceGuid, String email) {
        try {
            User user = userService.findByEmail(email);

            WorkExperience workExperience = findByGuid(workExperienceGuid);
            if (!user.equals(workExperience.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this item");
            }

            workExperience.setIsDeleted(true);
            workExperience.setUpdatedAt(ZonedDateTime.now());
            workExperience.setUpdatedBy(user.getId());

            workExperienceRepository.save(workExperience);
        } catch (Exception e) {
            throw e;
        }
    }
}
