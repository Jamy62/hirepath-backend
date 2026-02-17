package com.hirepath.hirepath_backend.controller.resume;

import com.hirepath.hirepath_backend.model.dto.province.ProvinceListDTO;
import com.hirepath.hirepath_backend.model.dto.resume.ResumeListDTO;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseFormat> resumeList(Principal principal) {
        List<ResumeListDTO> response = resumeService.resumeList(principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Resume list retrieved successfully"));
    }
}
