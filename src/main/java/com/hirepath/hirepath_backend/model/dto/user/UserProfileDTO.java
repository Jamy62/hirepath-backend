package com.hirepath.hirepath_backend.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileDTO {
    private String name;
    private String fullName;
    private String email;
    private String mobile;
    private String profile;
    private Role role;
    private Boolean isActive;
    private Boolean isBlocked;
    private String guid;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createdAt;
    private ZonedDateTime lastLoginAt;
}
