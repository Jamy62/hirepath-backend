package com.hirepath.hirepath_backend.model.request.paymentmethod;

import lombok.Data;

@Data
public class PaymentMethodUpdateRequest {
    private String name;
    private String description;
}
