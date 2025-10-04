package com.hirepath.hirepath_backend.controller.experiencelevel;

import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListDTO;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelCreateRequest;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.experiencelevel.ExperienceLevelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/experience-level")
public class ExperienceLevelController {

    private final ExperienceLevelService experienceLevelService;

    @PostMapping("/create/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> experienceLevelCreate(@Valid @RequestBody ExperienceLevelCreateRequest request, Principal principal) {
        experienceLevelService.experienceLevelCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Experience level created successfully"));
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> experienceLevelList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<ExperienceLevelListDTO> response = experienceLevelService.experienceLevelList(searchName, orderBy, first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Experience level list retrieved successfully"));
    }

    @PutMapping("/update/admin/{experienceLevelGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> experienceLevelUpdate(@PathVariable String experienceLevelGuid, @Valid @RequestBody ExperienceLevelUpdateRequest request, Principal principal) {
        experienceLevelService.experienceLevelUpdate(experienceLevelGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Experience level updated successfully"));
    }

    @DeleteMapping("/delete/admin/{experienceLevelGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> experienceLevelDelete(
            @PathVariable(value = "experienceLevelGuid") String experienceLevelGuid,
            Principal principal) {
        experienceLevelService.experienceLevelDelete(experienceLevelGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Experience level deleted successfully"));
    }
}
