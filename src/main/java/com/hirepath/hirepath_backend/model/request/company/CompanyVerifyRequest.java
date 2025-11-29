package com.hirepath.hirepath_backend.model.request.company;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.time.LocalDate;

@AutoNotBlank
@Data
public class CompanyVerifyRequest {
    private String legalBusinessName;
    private String publicName;
    private String website;
    private String industry;
    private LocalDate foundedDate;
    private String companySize;
    private String businessType;
}
