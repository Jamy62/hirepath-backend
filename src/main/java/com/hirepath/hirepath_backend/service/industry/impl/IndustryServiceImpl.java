package com.hirepath.hirepath_backend.service.industry.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.industry.IndustryListDTO;
import com.hirepath.hirepath_backend.model.dto.industry.IndustryListProjection;
import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.industry.IndustryCreateRequest;
import com.hirepath.hirepath_backend.model.request.industry.IndustryUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.industry.IndustryRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
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
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseFormat industryCreate(IndustryCreateRequest request, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Industry industry = Industry.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            industryRepository.save(industry);

            return ResponseFormat.createSuccessResponse(null, "Industry created successfully");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseFormat industryList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<IndustryListProjection> industryListProjections = industryRepository.findAllIndustriesAdminPanel(searchName, orderBy, first, max);

                List<IndustryListDTO> industries = industryListProjections.stream()
                        .map(p -> IndustryListDTO.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();

                return ResponseFormat.createSuccessResponse(industries, "Industry list retrieved successfully");
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseFormat industryUpdate(String industryGuid, IndustryUpdateRequest request, String email) {
        try {
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Industry industry = industryRepository.findByGuid(industryGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Industry not found"));

            if (request.getName() != null && !request.getName().isBlank()) {
                industry.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                industry.setDescription(request.getDescription());
            }

            industry.setUpdatedAt(ZonedDateTime.now());
            industry.setUpdatedBy(admin.getId());

            industryRepository.save(industry);

            return ResponseFormat.createSuccessResponse(null, "Industry updated successfully");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ResponseFormat industryDelete(String industryGuid, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Industry industry = industryRepository.findByGuid(industryGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Industry not found"));

            industry.setIsDeleted(true);
            industry.setUpdatedAt(ZonedDateTime.now());
            industry.setUpdatedBy(admin.getId());

            industryRepository.save(industry);

            return ResponseFormat.createSuccessResponse(null, "Industry deleted successfully");
        } catch (Exception e) {
            throw e;
        }
    }
}
