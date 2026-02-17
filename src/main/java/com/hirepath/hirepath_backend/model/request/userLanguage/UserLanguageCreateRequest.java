package com.hirepath.hirepath_backend.model.request.userLanguage;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class UserLanguageCreateRequest {
    private String languageGuid;
    private String proficiency;
}
