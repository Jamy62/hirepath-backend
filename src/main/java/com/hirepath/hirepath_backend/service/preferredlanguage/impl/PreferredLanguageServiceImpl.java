package com.hirepath.hirepath_backend.service.preferredlanguage.impl;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.repository.preferredlanguage.PreferredLanguageRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.preferredlanguage.PreferredLanguageService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class PreferredLanguageServiceImpl implements PreferredLanguageService {

    private final PreferredLanguageRepository preferredLanguageRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public PreferredLanguage findByGuid(String guid) {
        try {
            return preferredLanguageRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PreferredLanguage not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PreferredLanguage findByName(String name) {
        try {
            return preferredLanguageRepository.findByName(name)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PreferredLanguage not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void preferredLanguageUpdate(String guid, String email) {
        try {
            User user = userService.findByEmail(email);
            PreferredLanguage preferredLanguage = findByGuid(guid);

            user.setPreferredLanguage(preferredLanguage);
            user.setUpdatedBy(user.getId());
            user.setUpdatedAt(ZonedDateTime.now());

            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }
}
