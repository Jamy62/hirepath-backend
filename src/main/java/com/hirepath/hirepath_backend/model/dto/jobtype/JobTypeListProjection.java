package com.hirepath.hirepath_backend.model.dto.jobtype;

import java.time.ZonedDateTime;

public interface JobTypeListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
