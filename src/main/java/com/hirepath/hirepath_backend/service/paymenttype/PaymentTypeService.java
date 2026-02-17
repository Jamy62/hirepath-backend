package com.hirepath.hirepath_backend.service.paymenttype;

import com.hirepath.hirepath_backend.model.dto.paymenttype.PaymentTypeListDTO;
import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeUpdateRequest;

import java.util.List;
public interface PaymentTypeService {

    PaymentType findByGuid(String guid);
    void paymentTypeCreate(PaymentTypeCreateRequest request, String adminEmail);
    List<PaymentTypeListDTO> paymentTypeList(String searchName, String orderBy, int first, int max);
    void paymentTypeUpdate(String paymentTypeGuid, PaymentTypeUpdateRequest request, String email);
    void paymentTypeDelete(String paymentTypeGuid, String adminEmail);
}
