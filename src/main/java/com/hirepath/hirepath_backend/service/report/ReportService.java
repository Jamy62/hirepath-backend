package com.hirepath.hirepath_backend.service.report;

import com.hirepath.hirepath_backend.model.dto.report.*;

import java.util.List;

public interface ReportService {
    List<TrendingIndustryDTO> getTrendingIndustries();
    List<MostPopularJobDTO> getMostPopularJobs();
    List<MostPopularCompanyDTO> getMostPopularCompanies();
    List<MostActiveUserDTO> getMostActiveUsers();
    List<UserGrowthDTO> getUserGrowth();
    List<JobApplicationSuccessRateDTO> getJobApplicationSuccessRates();
    List<CompanyEmployeeGrowthDTO> getCompanyEmployeeGrowth(String companyGuid);
    List<EmployeesPerPositionDTO> getEmployeesPerPosition(String companyGuid);
}
