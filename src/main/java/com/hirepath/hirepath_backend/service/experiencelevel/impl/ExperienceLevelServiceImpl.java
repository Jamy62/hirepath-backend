package com.hirepath.hirepath_backend.service.experiencelevel.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListDTO;
import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListProjection;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelCreateRequest;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelUpdateRequest;
import com.hirepath.hirepath_backend.repository.experiencelevel.ExperienceLevelRepository;
import com.hirepath.hirepath_backend.service.experiencelevel.ExperienceLevelService;
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
public class ExperienceLevelServiceImpl implements ExperienceLevelService {

    private final ExperienceLevelRepository experienceLevelRepository;
    private final UserService userService;

    @Override
    public ExperienceLevel findByGuid(String guid) {
        try {
            return experienceLevelRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Experience level not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void experienceLevelCreate(ExperienceLevelCreateRequest request, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            ExperienceLevel experienceLevel = ExperienceLevel.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            experienceLevelRepository.save(experienceLevel);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ExperienceLevelListDTO> experienceLevelList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<ExperienceLevelListProjection> experienceLevelListProjections = experienceLevelRepository.findAllExperienceLevelsAdminPanel(searchName, orderBy, first, max);

                return experienceLevelListProjections.stream()
                        .map(p -> ExperienceLevelListDTO.builder()
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
    public void experienceLevelUpdate(String experienceLevelGuid, ExperienceLevelUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            ExperienceLevel experienceLevel = findByGuid(experienceLevelGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                experienceLevel.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                experienceLevel.setDescription(request.getDescription());
            }

            experienceLevel.setUpdatedAt(ZonedDateTime.now());
            experienceLevel.setUpdatedBy(admin.getId());

            experienceLevelRepository.save(experienceLevel);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void experienceLevelDelete(String experienceLevelGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            ExperienceLevel experienceLevel = findByGuid(experienceLevelGuid);

            experienceLevel.setIsDeleted(true);
            experienceLevel.setUpdatedAt(ZonedDateTime.now());
            experienceLevel.setUpdatedBy(admin.getId());

            experienceLevelRepository.save(experienceLevel);
        } catch (Exception e) {
            throw e;
        }
    }
}
