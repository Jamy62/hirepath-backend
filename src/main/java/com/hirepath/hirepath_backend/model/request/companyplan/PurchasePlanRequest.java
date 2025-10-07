package com.hirepath.hirepath_backend.model.request.companyplan;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@AutoNotBlank
@Data
public class PurchasePlanRequest {
    private String planGuid;
    private String paymentMethodGuid;
}
