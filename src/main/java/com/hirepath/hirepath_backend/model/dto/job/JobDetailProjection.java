package com.hirepath.hirepath_backend.model.dto.job;

import java.sql.Timestamp;

public interface JobDetailProjection {
    String getGuid();
    String getTitle();
    String getDescription();
    String getRequirements();
    String getBenefits();
    Double getMinSalary();
    Double getMaxSalary();
    String getJobType();
    String getExperienceLevel();
    String getLocation();
    String getCompanyGuid();
    String getCompanyName();
    String getCompanyLogo();
    Timestamp getPostedDate();
    Timestamp getExpireDate();
    Integer getIsApplied();
    Integer getIsEmployed();
}
