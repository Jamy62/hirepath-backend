package com.hirepath.hirepath_backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentMethodCreateRequest {

    @NotBlank(message = "Payment method name must not be blank")
    private String name;

    private String description;
}
