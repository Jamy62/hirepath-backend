package com.hirepath.hirepath_backend.controller.township;

import com.hirepath.hirepath_backend.model.request.township.TownshipCreateRequest;
import com.hirepath.hirepath_backend.model.request.township.TownshipUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.township.TownshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/township")
public class TownshipController {

    private final TownshipService townshipService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> createTownship(@Valid @RequestBody TownshipCreateRequest request, Principal principal) {
        ResponseFormat responseFormat = townshipService.townshipCreate(request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> listTownship(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = townshipService.townshipList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{townshipGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> updateTownship(@PathVariable String townshipGuid, @Valid @RequestBody TownshipUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = townshipService.townshipUpdate(townshipGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{townshipGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> deleteTownship(
            @PathVariable(value = "townshipGuid") String townshipGuid,
            Principal principal) {
        ResponseFormat responseFormat = townshipService.townshipDelete(townshipGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
