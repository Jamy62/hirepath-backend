package com.hirepath.hirepath_backend.model.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface PlanListProjection {
    String getName();
    String getDescription();
    BigDecimal getPrice();
    Integer getDurationInDays();
    String getFeatures();
    String getGuid();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}

