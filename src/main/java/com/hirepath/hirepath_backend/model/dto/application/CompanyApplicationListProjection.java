package com.hirepath.hirepath_backend.model.dto.application;

import java.sql.Timestamp;

public interface CompanyApplicationListProjection {
    String getApplicationGuid();
    String getJobTitle();
    String getApplicantName();
    String getResumeGuid();
    String getStatus();
    Timestamp getApplicationDate();
}
