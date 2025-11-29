package com.hirepath.hirepath_backend.service.job.impl;

import com.hirepath.hirepath_backend.model.dto.job.JobDetailDTO;
import com.hirepath.hirepath_backend.model.dto.job.JobDetailProjection;
import com.hirepath.hirepath_backend.model.dto.job.JobListDTO;
import com.hirepath.hirepath_backend.model.dto.job.JobListProjection;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import com.hirepath.hirepath_backend.model.entity.jobindustry.JobIndustry;
import com.hirepath.hirepath_backend.model.entity.jobtype.JobType;
import com.hirepath.hirepath_backend.model.entity.township.Township;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.job.JobCreateRequest;
import com.hirepath.hirepath_backend.repository.application.ApplicationRepository;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyplan.CompanyPlanRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.job.JobRepository;
import com.hirepath.hirepath_backend.repository.jobindustry.JobIndustryRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.experiencelevel.ExperienceLevelService;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
import com.hirepath.hirepath_backend.service.job.JobService;
import com.hirepath.hirepath_backend.service.jobfunction.JobFunctionService;
import com.hirepath.hirepath_backend.service.jobtype.JobTypeService;
import com.hirepath.hirepath_backend.service.township.TownshipService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final TownshipService townshipService;
    private final ExperienceLevelService experienceLevelService;
    private final JobTypeService jobTypeService;
    private final JobFunctionService jobFunctionService;
    private final IndustryService industryService;
    private final UserService userService;
    private final JobIndustryRepository jobIndustryRepository;
    private final CompanyPlanRepository companyPlanRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public Job findByGuid(String guid) {
        try {
            return jobRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void jobCreate(JobCreateRequest request, String email, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            User user = userService.findByEmail(email);
            CompanyUser companyUser = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(user, company)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company user not found"));

            Township township = townshipService.findByGuid(request.getTownshipGuid());
            ExperienceLevel experienceLevel = experienceLevelService.findByGuid(request.getExperienceLevelGuid());
            JobType jobType = jobTypeService.findByGuid(request.getJobTypeGuid());
            JobFunction jobFunction = jobFunctionService.findByGuid(request.getJobFunctionGuid());

            Optional<CompanyPlan> activePlanOpt = companyPlanRepository.findByCompanyAndIsActiveTrue(company);

            Job job = Job.builder()
                    .company(company)
                    .companyUser(companyUser)
                    .township(township)
                    .experienceLevel(experienceLevel)
                    .jobType(jobType)
                    .jobFunction(jobFunction)
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .requirements(request.getRequirements())
                    .benefits(request.getBenefits())
                    .minSalary(request.getMinSalary())
                    .maxSalary(request.getMaxSalary())
                    .postedDate(ZonedDateTime.now())
                    .expireDate(ZonedDateTime.now().plusDays(30))
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            if (activePlanOpt.isPresent()) {
                job.setCompanyPlan(activePlanOpt.get());
            }
            else {
                company.setAmount(company.getAmount().add(new BigDecimal("200000")));
                companyRepository.save(company);
            }

            Job savedJob = jobRepository.save(job);

            for (String industryGuid : request.getIndustryGuids()) {
                Industry industry = industryService.findByGuid(industryGuid);
                
                JobIndustry jobIndustry = JobIndustry.builder()
                        .job(savedJob)
                        .industry(industry)
                        .guid(UUID.randomUUID().toString())
                        .isDeleted(false)
                        .createdAt(ZonedDateTime.now())
                        .createdBy(user.getId())
                        .build();
                
                jobIndustryRepository.save(jobIndustry);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void deleteJob(String jobGuid, String email, String companyGuid) {
        try {
            Job job = findByGuid(jobGuid);
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);

            if (!job.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this job");
            }

            List<Application> applications = applicationRepository.findAllByJobAndIsDeletedFalse(job);
            for (Application application : applications) {
                if (!company.equals(application.getJob().getCompany())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to reject this application");
                }
                application.setStatus("REJECTED");
                application.setUpdatedAt(ZonedDateTime.now());
                application.setUpdatedBy(user.getId());
                applicationRepository.save(application);
            }

            job.setIsDeleted(true);
            job.setUpdatedAt(ZonedDateTime.now());
            job.setUpdatedBy(user.getId());
            jobRepository.save(job);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<JobListDTO> jobList(String searchTitle, String companyGuid, String provinceGuid, String townshipGuid,
                                    String jobTypeGuid, String experienceLevelGuid, String industryGuid, String jobFunctionGuid, Double salary, String userGuid, String orderBy, int first, int max) {
        try {
            Long userId = null;
            if (userGuid != null && !userGuid.isBlank()) {
                userId = userService.findByGuid(userGuid).getId();
            }

            List<JobListProjection> jobListProjections = jobRepository.findAllJobsForListView(searchTitle, companyGuid, provinceGuid, townshipGuid,
                    jobTypeGuid, experienceLevelGuid, industryGuid, jobFunctionGuid, salary, userId, orderBy, first, max);

            return jobListProjections.stream()
                    .map(p -> JobListDTO.builder()
                            .guid(p.getGuid())
                            .title(p.getTitle())
                            .minSalary(p.getMinSalary())
                            .maxSalary(p.getMaxSalary())
                            .location(p.getLocation())
                            .companyName(p.getCompanyName())
                            .companyLogo(p.getCompanyLogo())
                            .jobType(p.getJobType())
                            .experienceLevel(p.getExperienceLevel())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .isApplied(p.getIsApplied() == 1)
                            .isEmployed(p.getIsEmployed() == 1)
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public JobDetailDTO jobDetail(String jobGuid, String userGuid) {
        try {
            Long userId = null;
            if (userGuid != null && !userGuid.isBlank()) {
                userId = userService.findByGuid(userGuid).getId();
            }

            JobDetailProjection projection = jobRepository.findJobDetailByGuid(jobGuid, userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

            Job job = findByGuid(jobGuid);
            List<String> industries = jobIndustryRepository.findAllByJob(job).stream()
                    .map(jobIndustry -> jobIndustry.getIndustry().getName())
                    .collect(Collectors.toList());

            return JobDetailDTO.builder()
                    .guid(projection.getGuid())
                    .title(projection.getTitle())
                    .description(projection.getDescription())
                    .requirements(projection.getRequirements())
                    .benefits(projection.getBenefits())
                    .minSalary(projection.getMinSalary())
                    .maxSalary(projection.getMaxSalary())
                    .jobType(projection.getJobType())
                    .experienceLevel(projection.getExperienceLevel())
                    .location(projection.getLocation())
                    .companyGuid(projection.getCompanyGuid())
                    .companyName(projection.getCompanyName())
                    .companyLogo(projection.getCompanyLogo())
                    .industries(industries)
                    .postedDate(projection.getPostedDate() != null ? projection.getPostedDate().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .expireDate(projection.getExpireDate() != null ? projection.getExpireDate().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .isApplied(projection.getIsApplied() == 1)
                    .isEmployed(projection.getIsEmployed() == 1)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
