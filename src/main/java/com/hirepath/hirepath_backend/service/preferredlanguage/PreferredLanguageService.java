package com.hirepath.hirepath_backend.service.preferredlanguage;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.model.request.preferredlanguage.PreferredLanguageCreateRequest;

public interface PreferredLanguageService {
    PreferredLanguage findByGuid(String guid);
    PreferredLanguage findByName(String name);
    void preferredLanguageUpdate(String guid, String email);
}
