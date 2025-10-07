package com.hirepath.hirepath_backend.model.dto.paymenttype;

import java.time.ZonedDateTime;

public interface PaymentTypeListProjection {
    String getName();
    String getDescription();
    String getGuid();
    Boolean getIsDeleted();
    ZonedDateTime getCreatedAt();
    ZonedDateTime getUpdatedAt();
}
