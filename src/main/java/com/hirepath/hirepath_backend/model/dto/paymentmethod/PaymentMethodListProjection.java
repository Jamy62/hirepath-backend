package com.hirepath.hirepath_backend.model.dto.paymentmethod;

import java.time.ZonedDateTime;

public interface PaymentMethodListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
