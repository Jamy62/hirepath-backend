package com.hirepath.hirepath_backend.controller.paymenttype;

import com.hirepath.hirepath_backend.model.dto.paymenttype.PaymentTypeListDTO;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeCreateRequest;
import com.hirepath.hirepath_backend.model.request.paymenttype.PaymentTypeUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.paymenttype.PaymentTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment-type")
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> paymentTypeCreate(@Valid @RequestBody PaymentTypeCreateRequest request, Principal principal) {
        paymentTypeService.paymentTypeCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment type created successfully"));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseFormat> paymentTypeList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<PaymentTypeListDTO> response = paymentTypeService.paymentTypeList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Payment type list retrieved successfully"));
    }

    @PutMapping("/update/admin/{paymentTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> paymentTypeUpdate(@PathVariable String paymentTypeGuid, @Valid @RequestBody PaymentTypeUpdateRequest request, Principal principal) {
        paymentTypeService.paymentTypeUpdate(paymentTypeGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment type updated successfully"));
    }

    @DeleteMapping("/delete/admin/{paymentTypeGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> paymentTypeDelete(
            @PathVariable(value = "paymentTypeGuid") String paymentTypeGuid,
            Principal principal) {
        paymentTypeService.paymentTypeDelete(paymentTypeGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Payment type deleted successfully"));
    }
}
