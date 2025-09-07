package com.hirepath.hirepath_backend.controller.paymentmethod;

import com.hirepath.hirepath_backend.model.request.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment-method")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> createPaymentMethod(@Valid @RequestBody PaymentMethodCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = paymentMethodService.paymentMethodCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> listPaymentMethods(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = paymentMethodService.paymentMethodList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> updatePaymentMethod(@PathVariable String paymentMethodGuid, @Valid @RequestBody PaymentMethodUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = paymentMethodService.paymentMethodUpdate(paymentMethodGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> deletePaymentMethod(
            @PathVariable(value = "paymentMethodGuid") String paymentMethodGuid,
            Principal principal) {
        ResponseFormat responseFormat = paymentMethodService.paymentMethodDelete(paymentMethodGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
