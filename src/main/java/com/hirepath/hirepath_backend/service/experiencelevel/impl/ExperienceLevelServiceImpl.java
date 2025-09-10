package com.hirepath.hirepath_backend.service.experiencelevel.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListDTO;
import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListProjection;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelCreateRequest;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.experiencelevel.ExperienceLevelRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.experiencelevel.ExperienceLevelService;
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
    private final UserRepository userRepository;

    @Override
    public ResponseFormat experienceLevelCreate(ExperienceLevelCreateRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        ExperienceLevel experienceLevel = ExperienceLevel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(admin.getId())
                .build();

        experienceLevelRepository.save(experienceLevel);

        return ResponseFormat.createSuccessResponse(null, "Experience level created successfully");
    }

    @Override
    public ResponseFormat experienceLevelList(String searchName, String orderBy, int first, int max) {
        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            // This method will need to be created in ExperienceLevelRepository
            List<ExperienceLevelListProjection> experienceLevelListProjections = experienceLevelRepository.findAllExperienceLevelsAdminPanel(searchName, orderBy, first, max);

            List<ExperienceLevelListDTO> experienceLevels = experienceLevelListProjections.stream()
                    .map(p -> ExperienceLevelListDTO.builder()
                            .name(p.getName())
                            .description(p.getDescription())
                            .guid(p.getGuid())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();

            return ResponseFormat.createSuccessResponse(experienceLevels, "Experience level list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat experienceLevelUpdate(String experienceLevelGuid, ExperienceLevelUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        // This method will need to be created in ExperienceLevelRepository
        ExperienceLevel experienceLevel = experienceLevelRepository.findByGuid(experienceLevelGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Experience level not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            experienceLevel.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            experienceLevel.setDescription(request.getDescription());
        }

        experienceLevel.setUpdatedAt(ZonedDateTime.now());
        experienceLevel.setUpdatedBy(admin.getId());

        experienceLevelRepository.save(experienceLevel);

        return ResponseFormat.createSuccessResponse(null, "Experience level updated successfully");
    }

    @Override
    public ResponseFormat experienceLevelDelete(String experienceLevelGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        // This method will need to be created in ExperienceLevelRepository
        ExperienceLevel experienceLevel = experienceLevelRepository.findByGuid(experienceLevelGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Experience level not found"));

        experienceLevel.setIsDeleted(true);
        experienceLevel.setUpdatedAt(ZonedDateTime.now());
        experienceLevel.setUpdatedBy(admin.getId());

        experienceLevelRepository.save(experienceLevel);

        return ResponseFormat.createSuccessResponse(null, "Experience level deleted successfully");
    }
}
