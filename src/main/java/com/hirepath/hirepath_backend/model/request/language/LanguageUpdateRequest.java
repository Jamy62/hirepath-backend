package com.hirepath.hirepath_backend.model.request.language;

import lombok.Data;

@Data
public class LanguageUpdateRequest {
    private String name;
    private String code;
}
