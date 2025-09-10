package com.hirepath.hirepath_backend.model.dto.user;

import java.sql.Timestamp;

public interface UserListProjection {
    String getName();
    String getFullName();
    String getEmail();
    String getMobile();
    String getProfile();
    String getRoleName();
    Boolean getIsActive();
    Boolean getIsBlocked();
    String getGuid();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
    Timestamp getLastLoginAt();
}