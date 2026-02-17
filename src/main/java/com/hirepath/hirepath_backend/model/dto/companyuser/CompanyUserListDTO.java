package com.hirepath.hirepath_backend.model.dto.companyuser;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class CompanyUserListDTO {
    private String name;
    private String email;
    private String guid;
    private String userGuid;
    private String role;
    private String profilePicture;
    private ZonedDateTime joinedDate;
    private List<CompanyUserPositionsDTO> positions;
}
