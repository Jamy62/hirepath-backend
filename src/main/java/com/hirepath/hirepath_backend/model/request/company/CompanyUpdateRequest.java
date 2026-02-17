package com.hirepath.hirepath_backend.model.request.company;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;

@Data
public class CompanyUpdateRequest {
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
    private LocalDate foundedDate;
}
