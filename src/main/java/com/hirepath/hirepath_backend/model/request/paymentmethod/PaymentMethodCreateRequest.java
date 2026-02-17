package com.hirepath.hirepath_backend.model.request.paymentmethod;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class PaymentMethodCreateRequest {
    private String paymentTypeGuid;
    private String code;
    private String cvv;
}
