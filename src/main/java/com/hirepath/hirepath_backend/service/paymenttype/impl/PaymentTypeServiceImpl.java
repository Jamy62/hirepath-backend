package com.hirepath.hirepath_backend.service.paymenttype.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.paymenttype.PaymentTypeListDTO;
import com.hirepath.hirepath_backend.model.dto.paymenttype.PaymentTypeListProjection;
import com.hirepath.hirepath_backend.model.entity.paymenttype.PaymentType;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeUpdateRequest;
import com.hirepath.hirepath_backend.repository.paymenttype.PaymentTypeRepository;
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
public class PaymentTypeServiceImpl implements PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;
    private final UserService userService;

    @Override
    public PaymentType findByGuid(String guid) {
        try {
            return paymentTypeRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment type not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentTypeCreate(PaymentTypeCreateRequest request, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            PaymentType paymentType = PaymentType.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            paymentTypeRepository.save(paymentType);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PaymentTypeListDTO> paymentTypeList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<PaymentTypeListProjection> paymentTypeListProjections = paymentTypeRepository.findAllPaymentTypesAdminPanel(searchName, orderBy, first, max);

                return paymentTypeListProjections.stream()
                        .map(p -> PaymentTypeListDTO.builder()
                                .name(p.getName())
                                .description(p.getDescription())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().withZoneSameInstant(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().withZoneSameInstant(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentTypeUpdate(String paymentTypeGuid, PaymentTypeUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            PaymentType paymentType = findByGuid(paymentTypeGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                paymentType.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                paymentType.setDescription(request.getDescription());
            }

            paymentType.setUpdatedAt(ZonedDateTime.now());
            paymentType.setUpdatedBy(admin.getId());

            paymentTypeRepository.save(paymentType);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void paymentTypeDelete(String paymentTypeGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);

            PaymentType paymentType = findByGuid(paymentTypeGuid);

            paymentType.setIsDeleted(true);
            paymentType.setUpdatedAt(ZonedDateTime.now());
            paymentType.setUpdatedBy(admin.getId());

            paymentTypeRepository.save(paymentType);
        } catch (Exception e) {
            throw e;
        }
    }
}
