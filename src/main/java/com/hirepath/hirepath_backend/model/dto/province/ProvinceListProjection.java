package com.hirepath.hirepath_backend.model.dto.province;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface ProvinceListProjection {
    String getName();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
