package com.hirepath.hirepath_backend.service.userlanguage;

import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;

public interface UserLanguageService {
    UserLanguage findByGuid(String guid);
}
