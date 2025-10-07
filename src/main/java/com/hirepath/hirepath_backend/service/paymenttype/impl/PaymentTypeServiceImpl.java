package com.hirepath.hirepath_backend.service.paymenttype.impl;

import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import com.hirepath.hirepath_backend.repository.paymenttype.PaymentTypeRepository;
import com.hirepath.hirepath_backend.service.paymenttype.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    @Override
    public PaymentType findByGuid(String guid) {
        try {
            return paymentTypeRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PaymentType not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
