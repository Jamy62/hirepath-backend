package com.hirepath.hirepath_backend.service.paymentmethod;

import com.hirepath.hirepath_backend.model.request.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface PaymentMethodService {

    ResponseFormat paymentMethodCreate(PaymentMethodCreateRequest request, String adminEmail);

    ResponseFormat paymentMethodList(String searchName, String orderBy, int first, int max);

    ResponseFormat paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String email);

    ResponseFormat paymentMethodDelete(String paymentMethodGuid, String adminEmail);
}
