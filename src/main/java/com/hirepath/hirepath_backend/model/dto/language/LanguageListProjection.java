package com.hirepath.hirepath_backend.model.dto.language;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface LanguageListProjection {
    String getName();
    String getCode();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
