package com.hirepath.hirepath_backend.model.request.language;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class LanguageCreateRequest {
    private String name;
    private String code;
}
