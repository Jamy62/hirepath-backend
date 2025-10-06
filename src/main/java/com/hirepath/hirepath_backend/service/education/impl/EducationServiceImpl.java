package com.hirepath.hirepath_backend.service.education.impl;

import com.hirepath.hirepath_backend.model.entity.education.Education;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.education.EducationCreateRequest;
import com.hirepath.hirepath_backend.repository.education.EducationRepository;
import com.hirepath.hirepath_backend.service.education.EducationService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final UserService userService;

    @Override
    public Education findByGuid(String guid) {
        try {
            return educationRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void educationCreate(EducationCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            Education education = Education.builder()
                    .user(user)
                    .institution(request.getInstitution())
                    .degree(request.getDegree())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            educationRepository.save(education);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void educationDelete(String educationGuid, String email) {
        try {
            User user = userService.findByEmail(email);

            Education education = findByGuid(educationGuid);
            if (!user.equals(education.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this item");
            }

            education.setIsDeleted(true);
            education.setUpdatedAt(ZonedDateTime.now());
            education.setUpdatedBy(user.getId());

            educationRepository.save(education);
        } catch (Exception e) {
            throw e;
        }
    }
}
