package com.hirepath.hirepath_backend.model.dto.paymentmethod;

import java.sql.Timestamp;

public interface PaymentMethodListProjection {
    String getGuid();
    String getCardCode();
    String getPaymentTypeName();
    Timestamp getCreatedAt();
    Timestamp getUpdatedAt();
}
