package com.hirepath.hirepath_backend.controller.report;

import com.hirepath.hirepath_backend.model.dto.report.*;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/trending-industries")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getTrendingIndustries() {
        List<TrendingIndustryDTO> response = reportService.getTrendingIndustries();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Trending industries report"));
    }

    @GetMapping("/most-popular-jobs")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getMostPopularJobs() {
        List<MostPopularJobDTO> response = reportService.getMostPopularJobs();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Most popular jobs report"));
    }

    @GetMapping("/most-popular-companies")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getMostPopularCompanies() {
        List<MostPopularCompanyDTO> response = reportService.getMostPopularCompanies();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Most popular companies report"));
    }

    @GetMapping("/most-active-users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getMostActiveUsers() {
        List<MostActiveUserDTO> response = reportService.getMostActiveUsers();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Most active users report"));
    }

    @GetMapping("/user-growth")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getUserGrowth() {
        List<UserGrowthDTO> response = reportService.getUserGrowth();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "User growth report"));
    }

    @GetMapping("/job-application-success-rates")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> getJobApplicationSuccessRates() {
        List<JobApplicationSuccessRateDTO> response = reportService.getJobApplicationSuccessRates();
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Job application success rates report"));
    }

    @GetMapping("/company-employee-growth/{companyGuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ResponseFormat> getCompanyEmployeeGrowth(@PathVariable String companyGuid) {
        List<CompanyEmployeeGrowthDTO> response = reportService.getCompanyEmployeeGrowth(companyGuid);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Company employee growth report"));
    }

    @GetMapping("/employees-per-position/{companyGuid}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<ResponseFormat> getEmployeesPerPosition(@PathVariable String companyGuid) {
        List<EmployeesPerPositionDTO> response = reportService.getEmployeesPerPosition(companyGuid);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Employees per position report"));
    }
}
