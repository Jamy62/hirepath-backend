package com.hirepath.hirepath_backend.model.dto.jobfunction;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface JobFunctionListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
