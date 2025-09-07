package com.hirepath.hirepath_backend.model.dto;

import java.time.ZonedDateTime;

public interface TownshipListProjection {
    String getName();
    String getProvinceName();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
