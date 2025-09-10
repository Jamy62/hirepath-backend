package com.hirepath.hirepath_backend.service.paymentmethod.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListProjection;
import com.hirepath.hirepath_backend.model.entity.paymentmethod.PaymentMethod;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.paymentmethod.PaymentMethodRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
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
    private final UserRepository userRepository;

    @Override
    public ResponseFormat paymentMethodCreate(PaymentMethodCreateRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        PaymentMethod paymentMethod = PaymentMethod.builder()
                .name(request.getName())
                .description(request.getDescription())
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(admin.getId())
                .build();

        paymentMethodRepository.save(paymentMethod);

        return ResponseFormat.createSuccessResponse(null, "Payment method created successfully");
    }

    @Override
    public ResponseFormat paymentMethodList(String searchName, String orderBy, int first, int max) {
        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            List<PaymentMethodListProjection> paymentMethodListProjections = paymentMethodRepository.findAllPaymentMethodsAdminPanel(searchName, orderBy, first, max);

            List<PaymentMethodListDTO> paymentMethods = paymentMethodListProjections.stream()
                    .map(p -> PaymentMethodListDTO.builder()
                            .name(p.getName())
                            .description(p.getDescription())
                            .guid(p.getGuid())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();

            return ResponseFormat.createSuccessResponse(paymentMethods, "Payment method list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat paymentMethodUpdate(String paymentMethodGuid, PaymentMethodUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findByGuid(paymentMethodGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            paymentMethod.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            paymentMethod.setDescription(request.getDescription());
        }

        paymentMethod.setUpdatedAt(ZonedDateTime.now());
        paymentMethod.setUpdatedBy(admin.getId());

        paymentMethodRepository.save(paymentMethod);

        return ResponseFormat.createSuccessResponse(null, "Payment method updated successfully");
    }

    @Override
    public ResponseFormat paymentMethodDelete(String paymentMethodGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findByGuid(paymentMethodGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method not found"));

        paymentMethod.setIsDeleted(true);
        paymentMethod.setUpdatedAt(ZonedDateTime.now());
        paymentMethod.setUpdatedBy(admin.getId());

        paymentMethodRepository.save(paymentMethod);

        return ResponseFormat.createSuccessResponse(null, "Payment method deleted successfully");
    }
}
