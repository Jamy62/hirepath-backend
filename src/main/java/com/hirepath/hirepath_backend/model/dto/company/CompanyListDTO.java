package com.hirepath.hirepath_backend.model.dto.company;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class CompanyListDTO {
    private String name;
    private String logo;
    private String email;
    private String phone;
    private Company.VerificationStatus verificationStatus;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
