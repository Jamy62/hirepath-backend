package com.hirepath.hirepath_backend.service.job.impl;

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
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyplan.CompanyPlanRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.experiencelevel.ExperienceLevelRepository;
import com.hirepath.hirepath_backend.repository.industry.IndustryRepository;
import com.hirepath.hirepath_backend.repository.job.JobRepository;
import com.hirepath.hirepath_backend.repository.jobfunction.JobFunctionRepository;
import com.hirepath.hirepath_backend.repository.jobindustry.JobIndustryRepository;
import com.hirepath.hirepath_backend.repository.jobtype.JobTypeRepository;
import com.hirepath.hirepath_backend.repository.township.TownshipRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.experiencelevel.ExperienceLevelService;
import com.hirepath.hirepath_backend.service.industry.IndustryService;
import com.hirepath.hirepath_backend.service.job.JobService;
import com.hirepath.hirepath_backend.service.jobfunction.JobFunctionService;
import com.hirepath.hirepath_backend.service.jobtype.JobTypeService;
import com.hirepath.hirepath_backend.service.township.TownshipService;
import com.hirepath.hirepath_backend.service.user.UserService;
import com.hirepath.hirepath_backend.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

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
                        .createdBy(companyUser.getId())
                        .build();
                
                jobIndustryRepository.save(jobIndustry);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
