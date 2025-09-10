package com.hirepath.hirepath_backend.model.dto.company;

import com.hirepath.hirepath_backend.model.entity.company.Company;

import java.sql.Timestamp;

public interface CompanyListProjection {
    String getName();
    String getLogo();
    String getEmail();
    String getPhone();
    Company.VerificationStatus getVerificationStatus();
    String getGuid();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}

