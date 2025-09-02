package com.hirepath.hirepath_backend.model.dto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


public interface UserListProjection {
    String getName();
    String getFullName();
    String getEmail();
    String getMobile();
    String getProfile();
    String getRoleName();
    Boolean getIsActive();
    Boolean getIsBlocked();
    Boolean getIsDeleted();
    String getGuid();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getLastLoginAt();
}