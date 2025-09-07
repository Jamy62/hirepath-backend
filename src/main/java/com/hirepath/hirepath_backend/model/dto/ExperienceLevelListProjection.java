package com.hirepath.hirepath_backend.model.dto;

import java.time.ZonedDateTime;

public interface ExperienceLevelListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
