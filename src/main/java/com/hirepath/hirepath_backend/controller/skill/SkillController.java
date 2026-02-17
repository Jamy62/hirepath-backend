package com.hirepath.hirepath_backend.controller.skill;

import com.hirepath.hirepath_backend.model.request.skill.SkillCreateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.skill.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/skill")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> skillCreate(@Valid @RequestBody SkillCreateRequest request, Principal principal) {
        skillService.skillCreate(request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Skill created successfully"));
    }

    @PostMapping("/delete/{skillGuid}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> skillDelete(@PathVariable(value = "skillGuid") String skillGuid,
                                                      Principal principal) {
        skillService.skillDelete(skillGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "Skill deleted successfully"));
    }
}
