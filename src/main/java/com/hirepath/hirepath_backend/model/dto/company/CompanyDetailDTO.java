package com.hirepath.hirepath_backend.model.dto.company;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompanyDetailDTO {
    private String name;
    private String logo;
    private String email;
    private String phone;
    private String guid;
    private ZonedDateTime createdAt;

    private Company.VerificationStatus verificationStatus;
    private String legalBusinessName;
    private String publicName;
    private String website;
    private String industry;
    private ZonedDateTime foundedDate;
    private String companySize;
    private String businessType;
    private ZonedDateTime verified_at;
    private Long verifiedBy;
}
