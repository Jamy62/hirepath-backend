package com.hirepath.hirepath_backend.model.dto.plan;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface PlanListProjection {
    String getName();
    String getDescription();
    BigDecimal getPrice();
    String getDuration();
    Integer getDurationDays();
    String getFeatures();
    String getGuid();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}

