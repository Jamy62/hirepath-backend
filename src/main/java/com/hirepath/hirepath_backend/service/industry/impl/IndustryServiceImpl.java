package com.hirepath.hirepath_backend.service.industry.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.industry.IndustryListDTO;
import com.hirepath.hirepath_backend.model.dto.industry.IndustryListProjection;
import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.industry.IndustryCreateRequest;
import com.hirepath.hirepath_backend.model.request.industry.IndustryUpdateRequest;
import com.hirepath.hirepath_backend.repository.industry.IndustryRepository;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
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
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;
    private final UserService userService;

    @Override
    public Industry findByGuid(String guid) {
        try {
            return industryRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Industry not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void industryCreate(IndustryCreateRequest request, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            Industry industry = Industry.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            industryRepository.save(industry);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<IndustryListDTO> industryList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<IndustryListProjection> industryListProjections = industryRepository.findAllIndustriesAdminPanel(searchName, orderBy, first, max);

                return industryListProjections.stream()
                        .map(p -> IndustryListDTO.builder()
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
    public void industryUpdate(String industryGuid, IndustryUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            Industry industry = findByGuid(industryGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                industry.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                industry.setDescription(request.getDescription());
            }

            industry.setUpdatedAt(ZonedDateTime.now());
            industry.setUpdatedBy(admin.getId());

            industryRepository.save(industry);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void industryDelete(String industryGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            Industry industry = findByGuid(industryGuid);

            industry.setIsDeleted(true);
            industry.setUpdatedAt(ZonedDateTime.now());
            industry.setUpdatedBy(admin.getId());

            industryRepository.save(industry);
        } catch (Exception e) {
            throw e;
        }
    }
}
