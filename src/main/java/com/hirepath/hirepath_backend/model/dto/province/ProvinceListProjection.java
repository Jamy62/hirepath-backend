package com.hirepath.hirepath_backend.model.dto.province;

import java.sql.Timestamp;

public interface ProvinceListProjection {
    String getName();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
