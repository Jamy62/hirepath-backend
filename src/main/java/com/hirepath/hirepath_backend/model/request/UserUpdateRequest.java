package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class UserUpdateRequest {
    @NotBlank(message = "User update guid must not be black")
    private String userGuid;

    private String name;
    private String fullName;
    @Email(message = "Email must be a valid email address")
    private String email;
    private String mobile;
    private String profile;
    private Boolean isActive;
    private Boolean isBlocked;
    private Boolean isDeleted;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime lastLoginAt;
}
