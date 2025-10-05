package com.hirepath.hirepath_backend.service.userlanguage.impl;

import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import com.hirepath.hirepath_backend.repository.userlanguage.UserLanguageRepository;
import com.hirepath.hirepath_backend.service.userlanguage.UserLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserLanguageServiceImpl implements UserLanguageService {

    private final UserLanguageRepository userLanguageRepository;

    @Override
    public UserLanguage findByGuid(String guid) {
        try {
            return userLanguageRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserLanguage not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
