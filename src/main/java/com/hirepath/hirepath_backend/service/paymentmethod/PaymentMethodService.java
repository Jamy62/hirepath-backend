package com.hirepath.hirepath_backend.service.paymentmethod;

import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;

import java.util.List;

public interface PaymentMethodService {

    PaymentMethod findByGuid(String guid);

    void paymentMethodCreate(PaymentMethodCreateRequest request, String companyGuid);

    List<PaymentMethodListDTO> paymentMethodList(String companyGuid);

    void paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String companyGuid);

    void paymentMethodDelete(String paymentMethodGuid, String companyGuid);
}
