package com.hirepath.hirepath_backend.service.language.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.language.LanguageListDTO;
import com.hirepath.hirepath_backend.model.dto.language.LanguageListProjection;
import com.hirepath.hirepath_backend.model.entity.language.Language;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.language.LanguageCreateRequest;
import com.hirepath.hirepath_backend.model.request.language.LanguageUpdateRequest;
import com.hirepath.hirepath_backend.repository.language.LanguageRepository;
import com.hirepath.hirepath_backend.service.language.LanguageService;
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
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final UserService userService;

    @Override
    public Language findByGuid(String guid) {
        try {
            return languageRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void languageCreate(LanguageCreateRequest request, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            Language language = Language.builder()
                    .name(request.getName())
                    .code(request.getCode())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            languageRepository.save(language);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<LanguageListDTO> languageList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<LanguageListProjection> languageListProjections = languageRepository.findAllLanguagesAdminPanel(searchName, orderBy, first, max);

                return languageListProjections.stream()
                        .map(p -> LanguageListDTO.builder()
                                .name(p.getName())
                                .code(p.getCode())
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
    public void languageUpdate(String languageGuid, LanguageUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            Language language = findByGuid(languageGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                language.setName(request.getName());
            }
            if (request.getCode() != null && !request.getCode().isBlank()) {
                language.setCode(request.getCode());
            }

            language.setUpdatedAt(ZonedDateTime.now());
            language.setUpdatedBy(admin.getId());

            languageRepository.save(language);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void languageDelete(String languageGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            Language language = findByGuid(languageGuid);

            language.setIsDeleted(true);
            language.setUpdatedAt(ZonedDateTime.now());
            language.setUpdatedBy(admin.getId());

            languageRepository.save(language);
        } catch (Exception e) {
            throw e;
        }
    }
}
