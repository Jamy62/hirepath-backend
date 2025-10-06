package com.hirepath.hirepath_backend.service.userlanguage;

import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import com.hirepath.hirepath_backend.model.request.userLanguage.UserLanguageCreateRequest;

public interface UserLanguageService {
    UserLanguage findByGuid(String guid);
    void userLanguageCreate(UserLanguageCreateRequest request, String email);
    void userLanguageDelete(String userLanguageGuid, String email);
}
