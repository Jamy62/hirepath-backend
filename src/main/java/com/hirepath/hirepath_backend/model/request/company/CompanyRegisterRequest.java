package com.hirepath.hirepath_backend.model.request.company;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.Email;
import lombok.*;

@AutoNotBlank
@Getter
@Setter
public class CompanyRegisterRequest {
    private String name;
    private String logo;
    private String banner;
    private String description;
    private String phone;
    @Email(message = "Email must be a valid email address")
    private String email;
}
