package com.hirepath.hirepath_backend.service.paymentmethod.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListProjection;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.repository.paymentmethod.PaymentMethodRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
import com.hirepath.hirepath_backend.service.paymenttype.PaymentTypeService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final CompanyService companyService;
    private final PaymentTypeService paymentTypeService;

    @Override
    public PaymentMethod findByGuid(String guid) {
        try {
            return paymentMethodRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment method not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentMethodCreate(PaymentMethodCreateRequest request, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            PaymentType paymentType = paymentTypeService.findByGuid(request.getPaymentTypeGuid());

            PaymentMethod paymentMethod = PaymentMethod.builder()
                    .company(company)
                    .paymentType(paymentType)
                    .code(request.getCode())
                    .cvv(request.getCvv())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(company.getId())
                    .build();

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PaymentMethodListDTO> paymentMethodList(String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            List<PaymentMethodListProjection> paymentMethodListProjections = paymentMethodRepository.findAllPaymentMethodsAdminPanel(company.getId());

            return paymentMethodListProjections.stream()
                    .map(p -> PaymentMethodListDTO.builder()
                            .guid(p.getGuid())
                            .cardCode(p.getCardCode())
                            .paymentTypeName(p.getPaymentTypeName())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            PaymentMethod paymentMethod = findByGuid(paymentMethodGuid);

            if (!company.equals(paymentMethod.getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            if (request.getPaymentTypeGuid() != null && !request.getPaymentTypeGuid().isBlank()) {
                PaymentType paymentType = paymentTypeService.findByGuid(request.getPaymentTypeGuid());
                paymentMethod.setPaymentType(paymentType);
            }
            if (request.getCode() != null && !request.getCode().isBlank()) {
                paymentMethod.setCode(request.getCode());
            }
            if (request.getCvv() != null && !request.getCvv().isBlank()) {
                paymentMethod.setCvv(request.getCvv());
            }

            paymentMethod.setUpdatedAt(ZonedDateTime.now());
            paymentMethod.setUpdatedBy(company.getId());

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentMethodDelete(String paymentMethodGuid, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            PaymentMethod paymentMethod = findByGuid(paymentMethodGuid);

            if (!company.equals(paymentMethod.getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            paymentMethod.setIsDeleted(true);
            paymentMethod.setUpdatedAt(ZonedDateTime.now());
            paymentMethod.setUpdatedBy(company.getId());

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }
}
