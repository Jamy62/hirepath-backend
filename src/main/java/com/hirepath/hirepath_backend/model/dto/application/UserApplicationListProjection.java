package com.hirepath.hirepath_backend.model.dto.application;

import java.sql.Timestamp;

public interface UserApplicationListProjection {
    String getApplicationGuid();
    String getJobTitle();
    String getCompanyName();
    String getStatus();
    Timestamp getApplicationDate();
}
