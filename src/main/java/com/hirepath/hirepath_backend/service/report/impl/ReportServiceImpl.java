package com.hirepath.hirepath_backend.service.report.impl;

import com.hirepath.hirepath_backend.model.dto.report.*;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.industry.IndustryRepository;
import com.hirepath.hirepath_backend.repository.job.JobRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final IndustryRepository industryRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CompanyService companyService;

    @Override
    public List<TrendingIndustryDTO> getTrendingIndustries() {
        List<TrendingIndustryProjection> projections = industryRepository.findTrendingIndustries();
        return projections.stream()
                .map(p -> new TrendingIndustryDTO(p.getIndustryName(), p.getApplicationCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MostPopularJobDTO> getMostPopularJobs() {
        ZonedDateTime startDate = ZonedDateTime.now().minusMonths(1);
        List<MostPopularJobProjection> projections = jobRepository.findMostPopularJobs(startDate);
        return projections.stream()
                .map(p -> new MostPopularJobDTO(p.getJobTitle(), p.getApplicationCount(), p.getCompanyName(), p.getLogo(), p.getWebsite()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MostPopularCompanyDTO> getMostPopularCompanies() {
        List<MostPopularCompanyProjection> projections = companyRepository.findMostPopularCompanies();
        return projections.stream()
                .map(p -> new MostPopularCompanyDTO(p.getCompanyName(), p.getApplicationCount(), p.getLogo(), p.getWebsite()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MostActiveUserDTO> getMostActiveUsers() {
        List<MostActiveUserProjection> projections = userRepository.findMostActiveUsers();
        return projections.stream()
                .map(p -> new MostActiveUserDTO(p.getUserName(), p.getApplicationCount(), p.getProfile()))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserGrowthDTO> getUserGrowth() {
        List<UserGrowthProjection> projections = userRepository.findUserGrowth();
        return projections.stream()
                .map(p -> new UserGrowthDTO(p.getDate(), p.getUserCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationSuccessRateDTO> getJobApplicationSuccessRates() {
        List<JobApplicationSuccessRateProjection> projections = companyRepository.findJobApplicationSuccessRates();
        return projections.stream()
                .map(p -> new JobApplicationSuccessRateDTO(p.getCompanyName(), p.getSuccessRate(), p.getLogo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyEmployeeGrowthDTO> getCompanyEmployeeGrowth(String companyGuid) {
        Company company = companyService.findByGuid(companyGuid);
        List<CompanyEmployeeGrowthProjection> projections = companyUserRepository.findCompanyEmployeeGrowth(company.getId());
        return projections.stream()
                .map(p -> new CompanyEmployeeGrowthDTO(p.getDate(), p.getEmployeeCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeesPerPositionDTO> getEmployeesPerPosition(String companyGuid) {
        Company company = companyService.findByGuid(companyGuid);
        List<EmployeesPerPositionProjection> projections = companyUserRepository.findEmployeesPerPosition(company.getId());
        return projections.stream()
                .map(p -> new EmployeesPerPositionDTO(p.getPositionName(), p.getEmployeeCount()))
                .collect(Collectors.toList());
    }
}
