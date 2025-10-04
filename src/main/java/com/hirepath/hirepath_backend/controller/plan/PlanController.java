package com.hirepath.hirepath_backend.controller.plan;

import com.hirepath.hirepath_backend.model.dto.plan.PlanListDTO;
import com.hirepath.hirepath_backend.model.request.plan.PlanCreateRequest;
import com.hirepath.hirepath_backend.model.request.plan.PlanUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.plan.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan")
public class PlanController {

    private final PlanService planService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> planCreate(@Valid @RequestBody PlanCreateRequest request, Principal principal) {
        planService.planCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Plan created successfully"));
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> planList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<PlanListDTO> response = planService.planList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Plan list retrieved successfully"));
    }

    @PutMapping("/update/admin/{planGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> planUpdate(@PathVariable String planGuid, @Valid @RequestBody PlanUpdateRequest request, Principal principal) {
        planService.planUpdate(planGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Plan updated successfully"));
    }

    @DeleteMapping("/delete/admin/{planGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> planDelete(
            @PathVariable(value = "planGuid") String planGuid,
            Principal principal) {
        planService.planDelete(planGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Plan deleted successfully"));
    }
}
