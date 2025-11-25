package com.hirepath.hirepath_backend.model.response;

import com.hirepath.hirepath_backend.model.dto.company.CompanyDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponse {
    private String token;
    private UserDetailDTO user;
    private CompanyDetailDTO company;
    private String role;
    private String companyRole;
}
