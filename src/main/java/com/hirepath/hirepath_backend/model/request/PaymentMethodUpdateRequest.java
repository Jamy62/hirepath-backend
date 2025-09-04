package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentMethodUpdateRequest {

    @NotBlank(message = "Payment method GUID must not be blank")
    private String paymentMethodGuid;

    private String name;

    private String description;
}
