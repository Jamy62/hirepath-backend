package com.hirepath.hirepath_backend.model.dto;

import java.time.LocalDateTime;


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
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getLastLoginAt();
}