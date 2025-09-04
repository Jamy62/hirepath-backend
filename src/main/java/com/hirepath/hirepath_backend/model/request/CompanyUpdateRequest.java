package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CompanyUpdateRequest {
    @NotBlank(message = "Company guid must not be blank")
    private String companyGuid;

    private String name;
    private String logo;
    private String banner;
    private String description;
    private String phone;
    @Email(message = "Email must be a valid email address")
    private String email;
    private String legalBusinessName;
    private String publicName;
    private String website;
    private String industry;
    private String companySize;
    private String businessType;
}
