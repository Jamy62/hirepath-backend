package com.hirepath.hirepath_backend.service.company.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.company.CompanyDetailDTO;
import com.hirepath.hirepath_backend.model.dto.company.CompanyListDTO;
import com.hirepath.hirepath_backend.model.dto.company.CompanyListProjection;
import com.hirepath.hirepath_backend.model.dto.company.CompanyProfileDTO;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.company.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyRequest;
import com.hirepath.hirepath_backend.model.request.company.CompanyVerifyResponseRequest;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Override
    public Company findByGuid(String guid) {
        try {
            return companyRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    public void companyRegister(CompanyRegisterRequest request, String email) {
        try {
            User user = userService.findByEmail(email);
            Role role = roleRepository.findByName("COMPANY_OWNER")
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

            Company company = Company.builder()
                    .name(request.getName())
                    .logo(request.getLogo())
                    .banner(request.getBanner())
                    .description(request.getDescription())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .verificationStatus(Company.VerificationStatus.FALSE)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .build();
            companyRepository.save(company);

            CompanyUser companyUser = CompanyUser.builder()
                    .company(company)
                    .user(user)
                    .role(role)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();
            companyUserRepository.save(companyUser);
        } catch (Exception e) {
            throw e;
        }
    }

    public void companyVerify(String companyGuid, CompanyVerifyRequest request, String email) {
        try {
            Company company = findByGuid(companyGuid);

            company.setVerificationStatus(Company.VerificationStatus.PENDING);
            company.setLegalBusinessName(request.getLegalBusinessName());
            company.setPublicName(request.getPublicName());
            company.setWebsite(request.getWebsite());
            company.setIndustry(request.getIndustry());
            company.setFoundedDate(request.getFoundedDate());
            company.setCompanySize(request.getCompanySize());
            company.setBusinessType(request.getBusinessType());

            companyRepository.save(company);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void verifyResponse(String companyGuid, CompanyVerifyResponseRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);

            Company company = findByGuid(companyGuid);

            if (request.isResponse()) {
                company.setVerificationStatus(Company.VerificationStatus.TRUE);
            }
            else {
                company.setVerificationStatus(Company.VerificationStatus.FALSE);
            }

            company.setVerified_at(ZonedDateTime.now());
            company.setVerifiedBy(admin.getId());
            companyRepository.save(company);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CompanyListDTO> companyList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<CompanyListProjection> companyListProjection = companyRepository.findAllCompaniesAdminPanel(searchName, orderBy, first, max, Company.VerificationStatus.TRUE);

                return companyListProjection.stream()
                        .map(p -> CompanyListDTO.builder()
                                .name(p.getName())
                                .logo(p.getLogo())
                                .email(p.getEmail())
                                .phone(p.getPhone())
                                .verificationStatus(p.getVerificationStatus())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CompanyListDTO> companyVerifyList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<CompanyListProjection> companyListProjection = companyRepository.findAllCompaniesAdminPanel(searchName, orderBy, first, max, Company.VerificationStatus.PENDING);

                return companyListProjection.stream()
                        .map(p -> CompanyListDTO.builder()
                                .name(p.getName())
                                .logo(p.getLogo())
                                .email(p.getEmail())
                                .phone(p.getPhone())
                                .verificationStatus(p.getVerificationStatus())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void companyUpdate(String companyGuid, CompanyUpdateRequest request, String email) {
        try {
            User admin = userService.findByEmail(email);
            Company company = findByGuid(companyGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                company.setName(request.getName());
            }
            if (request.getLogo() != null && !request.getLogo().isBlank()) {
                company.setLogo(request.getLogo());
            }
            if (request.getBanner() != null && !request.getBanner().isBlank()) {
                company.setBanner(request.getBanner());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                company.setDescription(request.getDescription());
            }
            if (request.getPhone() != null && !request.getPhone().isBlank()) {
                company.setPhone(request.getPhone());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                company.setEmail(request.getEmail());
            }
            if (request.getLegalBusinessName() != null && !request.getLegalBusinessName().isBlank()) {
                company.setLegalBusinessName(request.getLegalBusinessName());
            }
            if (request.getPublicName() != null && !request.getPublicName().isBlank()) {
                company.setPublicName(request.getPublicName());
            }
            if (request.getWebsite() != null && !request.getWebsite().isBlank()) {
                company.setWebsite(request.getWebsite());
            }
            if (request.getIndustry() != null && !request.getIndustry().isBlank()) {
                company.setIndustry(request.getIndustry());
            }
            if (request.getCompanySize() != null && !request.getCompanySize().isBlank()) {
                company.setCompanySize(request.getCompanySize());
            }
            if (request.getBusinessType() != null && !request.getBusinessType().isBlank()) {
                company.setBusinessType(request.getBusinessType());
            }
            company.setUpdatedAt(ZonedDateTime.now());
            company.setUpdatedBy(admin.getId());

            companyRepository.save(company);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void companyDelete(String companyGuid, String adminEmail) {
        try {
            User admin = userService.findByEmail(adminEmail);
            Company company = findByGuid(companyGuid);

            company.setIsDeleted(true);
            company.setUpdatedAt(ZonedDateTime.now());
            company.setUpdatedBy(admin.getId());

            companyRepository.save(company);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public CompanyDetailDTO companyDetail(String companyGuid) {
        try {
            Company company = findByGuid(companyGuid);

            return CompanyDetailDTO.builder()
                    .name(company.getName())
                    .logo(company.getLogo())
                    .email(company.getEmail())
                    .phone(company.getPhone())
                    .guid(company.getGuid())
                    .createdAt(company.getCreatedAt() != null ? company.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .verificationStatus(company.getVerificationStatus())
                    .legalBusinessName(company.getLegalBusinessName())
                    .publicName(company.getPublicName())
                    .website(company.getWebsite())
                    .industry(company.getIndustry())
                    .foundedDate(company.getFoundedDate())
                    .companySize(company.getCompanySize())
                    .businessType(company.getBusinessType())
                    .verified_at(company.getVerified_at())
                    .verifiedBy(company.getVerifiedBy())
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public CompanyProfileDTO companyProfile(String companyGuid) {
        try {
            Company company = findByGuid(companyGuid);

            return CompanyProfileDTO.builder()
                    .name(company.getName())
                    .logo(company.getLogo())
                    .email(company.getEmail())
                    .phone(company.getPhone())
                    .guid(company.getGuid())
                    .createdAt(company.getCreatedAt() != null ? company.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .verificationStatus(company.getVerificationStatus())
                    .legalBusinessName(company.getLegalBusinessName())
                    .publicName(company.getPublicName())
                    .website(company.getWebsite())
                    .industry(company.getIndustry())
                    .foundedDate(company.getFoundedDate())
                    .companySize(company.getCompanySize())
                    .businessType(company.getBusinessType())
                    .verified_at(company.getVerified_at())
                    .verifiedBy(company.getVerifiedBy())
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
