package com.hirepath.hirepath_backend.service.preferredlanguage.impl;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.repository.preferredlanguage.PreferredLanguageRepository;
import com.hirepath.hirepath_backend.service.preferredlanguage.PreferredLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PreferredLanguageServiceImpl implements PreferredLanguageService {

    private final PreferredLanguageRepository preferredLanguageRepository;

    @Override
    public PreferredLanguage findByGuid(String guid) {
        try {
            return preferredLanguageRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PreferredLanguage not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
