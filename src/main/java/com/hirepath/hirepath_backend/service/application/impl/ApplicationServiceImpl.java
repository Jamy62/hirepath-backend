package com.hirepath.hirepath_backend.service.application.impl;

import com.hirepath.hirepath_backend.model.dto.application.ApplicationDetailDTO;
import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListProjection;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListDTO;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListProjection;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.entity.position.Position;
import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.application.ApplicationAcceptRequest;
import com.hirepath.hirepath_backend.model.request.application.ApplicationCreateRequest;
import com.hirepath.hirepath_backend.repository.application.ApplicationRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.companyuserposition.CompanyUserPositionRepository;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.job.JobService;
import com.hirepath.hirepath_backend.service.position.PositionService;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import com.hirepath.hirepath_backend.service.role.RoleService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
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
    private final CompanyUserRepository companyUserRepository;
    private final PositionService positionService;
    private final CompanyUserPositionRepository companyUserPositionRepository;
    private final RoleService roleService;

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
    public List<Application> findAllByUserAndCompanyAndStatus(User user, Company company, String status) {
        try {
            return applicationRepository.findAllByUserAndCompanyAndStatusAndIsDeletedFalse(user, company, status);
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
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own the resume you are trying to apply with");
            }

            Optional<CompanyUser> companyUserOpt = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(user, job.getCompany());
            if (companyUserOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You already belong to this company");
            }

            Optional<Application> applicationOpt = applicationRepository.findByUserAndJobAndStatusAndIsDeletedFalse(user, job, "PENDING");
            if (applicationOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have already sent an application to this job");
            }

            Application application = Application.builder()
                    .user(user)
                    .job(job)
                    .resume(resume)
                    .coverLetter(request.getCoverLetter())
                    .applicationDate(ZonedDateTime.now())
                    .status("PENDING")
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
    public void acceptApplication(String applicationGuid, ApplicationAcceptRequest request, String email, String companyGuid) {
        try {
            User accepter = userService.findByEmail(email);
            Application application = findByGuid(applicationGuid);
            Company company = companyService.findByGuid(companyGuid);
            User applicant = application.getUser();

            if (!company.equals(application.getJob().getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to accept this application");
            }

            Role role = roleService.findByGuid(request.getRoleGuid());

            CompanyUser companyUser = companyUserRepository.findByUserAndCompanyIncludingDeleted(applicant.getId(), company.getId())
                    .map(existingUser -> {
                        existingUser.setRole(role);
                        existingUser.setIsDeleted(false);
                        existingUser.setUpdatedAt(ZonedDateTime.now());
                        existingUser.setUpdatedBy(accepter.getId());
                        return companyUserRepository.save(existingUser);
                    })
                    .orElseGet(() -> {
                        CompanyUser newCompanyUser = CompanyUser.builder()
                                .user(applicant)
                                .company(company)
                                .role(role)
                                .guid(UUID.randomUUID().toString())
                                .isDeleted(false)
                                .createdAt(ZonedDateTime.now())
                                .createdBy(accepter.getId())
                                .build();
                        return companyUserRepository.save(newCompanyUser);
                    });

            if (request.getPositionGuids() != null) {
                for (String positionGuid : request.getPositionGuids()) {
                    Position position = positionService.findByGuid(positionGuid);

                    CompanyUserPosition companyUserPosition = CompanyUserPosition.builder()
                            .companyUser(companyUser)
                            .position(position)
                            .guid(UUID.randomUUID().toString())
                            .isDeleted(false)
                            .createdAt(ZonedDateTime.now())
                            .createdBy(accepter.getId())
                            .build();

                    companyUserPositionRepository.save(companyUserPosition);
                }
            }

            application.setStatus("ACCEPTED");
            application.setUpdatedAt(ZonedDateTime.now());
            application.setUpdatedBy(accepter.getId());

            applicationRepository.save(application);

            List<Application> otherApplications = findAllByUserAndCompanyAndStatus(applicant, company, "PENDING");
            for (Application otherApplication : otherApplications) {
                if (!otherApplication.equals(application)) {
                    rejectApplication(otherApplication.getGuid(), email, companyGuid);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void rejectApplication(String applicationGuid, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Application application = findByGuid(applicationGuid);
            Company company = companyService.findByGuid(companyGuid);

            if (!company.equals(application.getJob().getCompany())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to reject this application");
            }

            application.setStatus("REJECTED");
            application.setUpdatedAt(ZonedDateTime.now());
            application.setUpdatedBy(user.getId());

            applicationRepository.save(application);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<UserApplicationListDTO> userApplications(String email) {
        try {
            User user = userService.findByEmail(email);
            List<UserApplicationListProjection> userApplicationListProjections = applicationRepository.findAllByUser(user.getId());

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
            List<CompanyApplicationListProjection> companyApplicationListProjections = applicationRepository.findAllByCompany(company.getId());

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

    @Override
    public ApplicationDetailDTO applicationDetail(String applicationGuid, String email) {
        try {
            User viewer = userService.findByEmail(email);
            Application application = findByGuid(applicationGuid);
            User sender = application.getUser();
            Company company = application.getJob().getCompany();

            Optional<CompanyUser> companyUserOpt = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(viewer, company);
            if (!viewer.equals(sender) && companyUserOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this application");
            }

            return ApplicationDetailDTO.builder()
                    .applicationGuid(application.getGuid())
                    .applicationStatus(application.getStatus())
                    .applicationDate(application.getApplicationDate())
                    .coverLetter(application.getCoverLetter())
                    .resumeFilePath(application.getResume().getFilePath())
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
