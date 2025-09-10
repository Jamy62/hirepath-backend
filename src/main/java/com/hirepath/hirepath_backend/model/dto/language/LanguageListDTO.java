package com.hirepath.hirepath_backend.model.dto.language;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class LanguageListDTO {
    private String name;
    private String code;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
