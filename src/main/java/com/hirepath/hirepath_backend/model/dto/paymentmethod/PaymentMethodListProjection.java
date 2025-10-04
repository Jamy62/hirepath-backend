package com.hirepath.hirepath_backend.model.dto.paymentmethod;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

public interface PaymentMethodListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
