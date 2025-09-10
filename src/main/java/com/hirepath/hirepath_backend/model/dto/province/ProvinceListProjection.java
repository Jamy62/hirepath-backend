package com.hirepath.hirepath_backend.model.dto.province;

import java.time.ZonedDateTime;

public interface ProvinceListProjection {
    String getName();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
