package com.hirepath.hirepath_backend.model.dto.job;

import java.sql.Timestamp;

public interface JobListProjection {
    String getGuid();
    String getTitle();
    String getMinSalary();
    String getMaxSalary();
    String getLocation();
    String getCompanyName();
    String getCompanyLogo();
    String getJobType();
    String getExperienceLevel();
    Timestamp getCreatedAt();
    Integer getIsApplied();
    Integer getIsEmployed();
}
