package com.hirepath.hirepath_backend.model.request.paymentmethod;

import lombok.Data;

@Data
public class PaymentMethodUpdateRequest {
    private String paymentTypeGuid;
    private String code;
    private String cvv;
}
