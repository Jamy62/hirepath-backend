package com.hirepath.hirepath_backend.model.dto.industry;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface IndustryListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
