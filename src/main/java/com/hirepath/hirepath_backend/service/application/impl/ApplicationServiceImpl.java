package com.hirepath.hirepath_backend.service.application.impl;

import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListProjection;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListProjection;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;
import com.hirepath.hirepath_backend.repository.application.ApplicationRepository;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.job.JobService;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final JobService jobService;
    private final ResumeService resumeService;
    private final CompanyService companyService;

    @Override
    public Application findByGuid(String guid) {
        try {
            return applicationRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void apply(ApplicationCreateRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            Job job = jobService.findByGuid(request.getJobGuid());
            Resume resume = resumeService.findByGuid(request.getResumeGuid());

            if (!user.equals(resume.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own the resume you are trying to apply with.");
            }

            Application application = Application.builder()
                    .user(user)
                    .job(job)
                    .resume(resume)
                    .applicationDate(ZonedDateTime.now())
                    .status("Applied")
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            applicationRepository.save(application);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<UserApplicationListDTO> userApplications(String email) {
        try {
            User user = userService.findByEmail(email);
            List<UserApplicationListProjection> userApplicationListProjections = applicationRepository.findAllForUser(user.getId());

            return userApplicationListProjections.stream()
                    .map(p -> UserApplicationListDTO.builder()
                            .applicationGuid(p.getApplicationGuid())
                            .jobTitle(p.getJobTitle())
                            .companyName(p.getCompanyName())
                            .status(p.getStatus())
                            .applicationDate(p.getApplicationDate() != null ? p.getApplicationDate().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CompanyApplicationListDTO> companyApplications(String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            List<CompanyApplicationListProjection> companyApplicationListProjections = applicationRepository.findAllForCompany(company.getId());

            return companyApplicationListProjections.stream()
                    .map(p -> CompanyApplicationListDTO.builder()
                            .applicationGuid(p.getApplicationGuid())
                            .jobTitle(p.getJobTitle())
                            .applicantName(p.getApplicantName())
                            .resumeGuid(p.getResumeGuid())
                            .status(p.getStatus())
                            .applicationDate(p.getApplicationDate() != null ? p.getApplicationDate().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }
}
