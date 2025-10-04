package com.hirepath.hirepath_backend.model.dto.user;

import java.sql.Timestamp;

public interface UserDetailProjection {
    String getName();
    String getFullName();
    String getEmail();
    String getMobile();
    String getProfile();
    String getRole();
    Boolean getIsActive();
    Boolean getIsBlocked();
    String getGuid();
    Timestamp getCreatedAt();
    Timestamp getLastLoginAt();
}
