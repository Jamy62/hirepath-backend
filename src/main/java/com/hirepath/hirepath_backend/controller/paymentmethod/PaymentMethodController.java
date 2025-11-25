package com.hirepath.hirepath_backend.controller.paymentmethod;

import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodCreate(@Valid @RequestBody PaymentMethodCreateRequest request, Principal principal, Authentication authentication) {
        paymentMethodService.paymentMethodCreate(request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method created successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodList(Authentication authentication) {
        List<PaymentMethodListDTO> response = paymentMethodService.paymentMethodList(AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Payment method list retrieved successfully"));
    }

    @PutMapping("/update/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodUpdate(@PathVariable String paymentMethodGuid, @Valid @RequestBody PaymentMethodUpdateRequest request, Principal principal, Authentication authentication) {
        paymentMethodService.paymentMethodUpdate(paymentMethodGuid, request, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method updated successfully"));
    }

    @DeleteMapping("/delete/admin/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodDelete(@PathVariable(value = "paymentMethodGuid") String paymentMethodGuid, Principal principal, Authentication authentication) {
        paymentMethodService.paymentMethodDelete(paymentMethodGuid, principal.getName(), AuthenticationUtil.getGuid(authentication.getDetails()));
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method deleted successfully"));
    }
}
