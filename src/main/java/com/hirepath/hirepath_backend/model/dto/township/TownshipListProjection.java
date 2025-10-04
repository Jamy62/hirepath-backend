package com.hirepath.hirepath_backend.model.dto.township;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface TownshipListProjection {
    String getName();
    String getProvinceName();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
