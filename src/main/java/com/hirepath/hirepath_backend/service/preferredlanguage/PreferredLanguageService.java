package com.hirepath.hirepath_backend.service.preferredlanguage;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;

public interface PreferredLanguageService {
    PreferredLanguage findByGuid(String guid);
}
