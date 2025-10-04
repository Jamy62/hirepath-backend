package com.hirepath.hirepath_backend.service.paymentmethod;

import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;

import java.util.List;

public interface PaymentMethodService {

    void paymentMethodCreate(PaymentMethodCreateRequest request, String adminEmail);

    List<PaymentMethodListDTO> paymentMethodList(String searchName, String orderBy, int first, int max);

    void paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String email);

    void paymentMethodDelete(String paymentMethodGuid, String adminEmail);
}
