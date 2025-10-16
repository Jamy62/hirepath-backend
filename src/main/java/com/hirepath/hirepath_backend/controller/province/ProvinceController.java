package com.hirepath.hirepath_backend.controller.province;

import com.hirepath.hirepath_backend.model.dto.province.ProvinceListDTO;
import com.hirepath.hirepath_backend.model.request.province.ProvinceCreateRequest;
import com.hirepath.hirepath_backend.model.request.province.ProvinceUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.province.ProvinceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/province")
public class ProvinceController {

    private final ProvinceService provinceService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> provinceCreate(@Valid @RequestBody ProvinceCreateRequest request, Principal principal) {
        provinceService.provinceCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Province created successfully"));
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> provinceList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<ProvinceListDTO> response = provinceService.provinceList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Province list retrieved successfully"));
    }

    @PutMapping("/update/admin/{provinceGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> provinceUpdate(@PathVariable String provinceGuid, @Valid @RequestBody ProvinceUpdateRequest request, Principal principal) {
        provinceService.provinceUpdate(provinceGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Province updated successfully"));
    }

    @DeleteMapping("/delete/admin/{provinceGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> provinceDelete(
            @PathVariable(value = "provinceGuid") String provinceGuid,
            Principal principal) {
        provinceService.provinceDelete(provinceGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Province deleted successfully"));
    }
}
