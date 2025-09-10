package com.hirepath.hirepath_backend.controller.province;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/province")
public class ProvinceController {

    private final ProvinceService provinceService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> createProvince(@Valid @RequestBody ProvinceCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = provinceService.provinceCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> listProvinces(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = provinceService.provinceList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{provinceGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> updateProvince(@PathVariable String provinceGuid, @Valid @RequestBody ProvinceUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = provinceService.provinceUpdate(provinceGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{provinceGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> deleteProvince(
            @PathVariable(value = "provinceGuid") String provinceGuid,
            Principal principal) {
        ResponseFormat responseFormat = provinceService.provinceDelete(provinceGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
