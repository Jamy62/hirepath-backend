package com.hirepath.hirepath_backend.model.dto.job;

import java.sql.Timestamp;

public interface JobListProjection {
    String getGuid();
    String getTitle();
    String getSalary();
    String getLocation();
    String getCompanyName();
    String getCompanyLogo();
    Boolean getIsCompanyVerified();
    String getJobType();
    String getExperienceLevel();
    Timestamp getCreatedAt();
}
