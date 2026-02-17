package com.hirepath.hirepath_backend.model.request.preferredlanguage;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class PreferredLanguageCreateRequest {
    private String name;
    private String code;
}
