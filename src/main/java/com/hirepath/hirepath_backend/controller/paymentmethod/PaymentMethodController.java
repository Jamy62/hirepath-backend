package com.hirepath.hirepath_backend.controller.paymentmethod;

import com.hirepath.hirepath_backend.model.dto.paymentmethod.PaymentMethodListDTO;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymentmethod.PaymentMethodUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.paymentmethod.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<ResponseFormat> paymentMethodCreate(@Valid @RequestBody PaymentMethodCreateRequest request, Principal principal) {
        paymentMethodService.paymentMethodCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method created successfully"));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<PaymentMethodListDTO> response = paymentMethodService.paymentMethodList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Payment method list retrieved successfully"));
    }

    @PutMapping("/update/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodUpdate(@PathVariable String paymentMethodGuid, @Valid @RequestBody PaymentMethodUpdateRequest request, Principal principal) {
        paymentMethodService.paymentMethodUpdate(paymentMethodGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method updated successfully"));
    }

    @DeleteMapping("/delete/admin/{paymentMethodGuid}")
    @PreAuthorize("hasAnyRole('COMPANY_OWNER')")
    public ResponseEntity<ResponseFormat> paymentMethodDelete(
            @PathVariable(value = "paymentMethodGuid") String paymentMethodGuid,
            Principal principal) {
        paymentMethodService.paymentMethodDelete(paymentMethodGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment method deleted successfully"));
    }
}
