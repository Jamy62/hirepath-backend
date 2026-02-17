package com.hirepath.hirepath_backend.service.userlanguage.impl;

import com.hirepath.hirepath_backend.model.entity.language.Language;
import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import com.hirepath.hirepath_backend.model.request.userLanguage.UserLanguageCreateRequest;
import com.hirepath.hirepath_backend.repository.userlanguage.UserLanguageRepository;
import com.hirepath.hirepath_backend.service.language.LanguageService;
import com.hirepath.hirepath_backend.service.user.UserService;
import com.hirepath.hirepath_backend.service.userlanguage.UserLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserLanguageServiceImpl implements UserLanguageService {

    private final UserLanguageRepository userLanguageRepository;
    private final LanguageService languageService;
    private final UserService userService;

    @Override
    public UserLanguage findByGuid(String guid) {
        try {
            return userLanguageRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserLanguage not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void userLanguageCreate(UserLanguageCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            Language language = languageService.findByGuid(request.getLanguageGuid());
            UserLanguage userLanguage = UserLanguage.builder()
                    .user(user)
                    .language(language)
                    .proficiency(request.getProficiency())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            userLanguageRepository.save(userLanguage);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void userLanguageDelete(String userLanguageGuid, String email) {
        try {
            User user = userService.findByEmail(email);
            UserLanguage userLanguage = findByGuid(userLanguageGuid);
            if (!user.equals(userLanguage.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this item");
            }

            userLanguage.setIsDeleted(true);
            userLanguage.setUpdatedAt(ZonedDateTime.now());
            userLanguage.setUpdatedBy(user.getId());

            userLanguageRepository.save(userLanguage);
        } catch (Exception e) {
            throw e;
        }
    }
}
