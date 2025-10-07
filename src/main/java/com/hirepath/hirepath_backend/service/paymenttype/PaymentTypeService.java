package com.hirepath.hirepath_backend.service.paymenttype;

import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;

public interface PaymentTypeService {
    PaymentType findByGuid(String guid);
}
