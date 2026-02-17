package com.hirepath.hirepath_backend.model.request.paymenttype;

import lombok.Data;

@Data
public class PaymentTypeUpdateRequest {
    private String name;
    private String description;
}
