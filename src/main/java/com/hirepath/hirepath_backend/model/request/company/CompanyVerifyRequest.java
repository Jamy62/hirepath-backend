package com.hirepath.hirepath_backend.model.request.company;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@AutoNotBlank
@Data
public class CompanyVerifyRequest {
    private String legalBusinessName;
    private String publicName;
    private String website;
    private String industry;
    private ZonedDateTime foundedDate;
    private String companySize;
    private String businessType;
}
