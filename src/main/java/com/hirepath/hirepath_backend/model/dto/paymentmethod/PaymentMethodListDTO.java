package com.hirepath.hirepath_backend.model.dto.paymentmethod;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class PaymentMethodListDTO {
    private String guid;
    private String cardCode;
    private String paymentTypeName;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
