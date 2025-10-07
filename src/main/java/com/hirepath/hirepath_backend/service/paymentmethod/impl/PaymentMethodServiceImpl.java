package com.hirepath.hirepath_backend.service.paymentmethod.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListProjection;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.repository.paymentmethod.PaymentMethodRepository;
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
    private final UserService userService;
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
    public void paymentMethodCreate(PaymentMethodCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            PaymentType paymentType = paymentTypeService.findByGuid(request.getPaymentTypeGuid());

            PaymentMethod paymentMethod = PaymentMethod.builder()
                    .user(user)
                    .paymentType(paymentType)
                    .cardCode(request.getCardCode())
                    .cvvNumber(request.getCvvNumber())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PaymentMethodListDTO> paymentMethodList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<PaymentMethodListProjection> paymentMethodListProjections = paymentMethodRepository.findAllPaymentMethodsAdminPanel(searchName, orderBy, first, max);

                return paymentMethodListProjections.stream()
                        .map(p -> PaymentMethodListDTO.builder()
                                .guid(p.getGuid())
                                .cardCode(p.getCardCode())
                                .paymentTypeName(p.getPaymentTypeName())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            PaymentMethod paymentMethod = findByGuid(paymentMethodGuid);

            if (!user.equals(paymentMethod.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            if (request.getPaymentTypeGuid() != null && !request.getPaymentTypeGuid().isBlank()) {
                PaymentType paymentType = paymentTypeService.findByGuid(request.getPaymentTypeGuid());
                paymentMethod.setPaymentType(paymentType);
            }
            if (request.getCardCode() != null && !request.getCardCode().isBlank()) {
                paymentMethod.setCardCode(request.getCardCode());
            }
            if (request.getCvvNumber() != null && !request.getCvvNumber().isBlank()) {
                paymentMethod.setCvvNumber(request.getCvvNumber());
            }

            paymentMethod.setUpdatedAt(ZonedDateTime.now());
            paymentMethod.setUpdatedBy(user.getId());

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentMethodDelete(String paymentMethodGuid, String email) {
        try {
            User user = userService.findByEmail(email);
            PaymentMethod paymentMethod = findByGuid(paymentMethodGuid);

            if (!user.equals(paymentMethod.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this payment method");
            }

            paymentMethod.setIsDeleted(true);
            paymentMethod.setUpdatedAt(ZonedDateTime.now());
            paymentMethod.setUpdatedBy(user.getId());

            paymentMethodRepository.save(paymentMethod);
        } catch (Exception e) {
            throw e;
        }
    }
}
