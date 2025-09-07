package com.hirepath.hirepath_backend.service.language.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.LanguageListDTO;
import com.hirepath.hirepath_backend.model.dto.LanguageListProjection;
import com.hirepath.hirepath_backend.model.entity.language.Language;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.LanguageCreateRequest;
import com.hirepath.hirepath_backend.model.request.LanguageUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.language.LanguageRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.language.LanguageService;
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
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseFormat languageCreate(LanguageCreateRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Language language = Language.builder()
                .name(request.getName())
                .code(request.getCode())
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(admin.getId())
                .build();

        languageRepository.save(language);

        return ResponseFormat.createSuccessResponse(null, "Language created successfully");
    }

    @Override
    public ResponseFormat languageList(String searchName, String orderBy, int first, int max) {
        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            // This method will need to be created in LanguageRepository
            List<LanguageListProjection> languageListProjections = languageRepository.findAllLanguagesAdminPanel(searchName, orderBy, first, max);

            List<LanguageListDTO> languages = languageListProjections.stream()
                    .map(p -> LanguageListDTO.builder()
                            .name(p.getName())
                            .code(p.getCode())
                            .guid(p.getGuid())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();

            return ResponseFormat.createSuccessResponse(languages, "Language list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat languageUpdate(String languageGuid, LanguageUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Language language = languageRepository.findByGuid(languageGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            language.setName(request.getName());
        }
        if (request.getCode() != null && !request.getCode().isBlank()) {
            language.setCode(request.getCode());
        }

        language.setUpdatedAt(ZonedDateTime.now());
        language.setUpdatedBy(admin.getId());

        languageRepository.save(language);

        return ResponseFormat.createSuccessResponse(null, "Language updated successfully");
    }

    @Override
    public ResponseFormat languageDelete(String languageGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Language language = languageRepository.findByGuid(languageGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language not found"));

        language.setIsDeleted(true);
        language.setUpdatedAt(ZonedDateTime.now());
        language.setUpdatedBy(admin.getId());

        languageRepository.save(language);

        return ResponseFormat.createSuccessResponse(null, "Language deleted successfully");
    }
}
