package com.hirepath.hirepath_backend.controller.plan;

import com.hirepath.hirepath_backend.model.request.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.PlanUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.plan.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan")
public class PlanController {

    private final PlanService planService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> createPlan(@Valid @RequestBody PlanCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = planService.planCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> listPlans(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = planService.planList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{planGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> updatePlan(@PathVariable String planGuid, @Valid @RequestBody PlanUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = planService.planUpdate(planGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{planGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> deletePlan(
            @PathVariable(value = "planGuid") String planGuid,
            Principal principal) {
        ResponseFormat responseFormat = planService.planDelete(planGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
