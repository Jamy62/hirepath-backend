package com.hirepath.hirepath_backend.model.request;

import lombok.Data;

@Data
public class PaymentMethodUpdateRequest {
    private String name;
    private String description;
}
