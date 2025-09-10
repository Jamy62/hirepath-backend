package com.hirepath.hirepath_backend.model.dto.language;

import java.time.ZonedDateTime;

public interface LanguageListProjection {
    String getName();
    String getCode();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
