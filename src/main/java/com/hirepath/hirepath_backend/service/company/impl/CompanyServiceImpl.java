package com.hirepath.hirepath_backend.service.company.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.CompanyListDTO;
import com.hirepath.hirepath_backend.model.dto.CompanyListProjection;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.CompanyRegisterRequest;
import com.hirepath.hirepath_backend.model.request.CompanyUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
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
    private final UserRepository userRepository;

    public ResponseFormat companyRegister(CompanyRegisterRequest request) {
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

        return ResponseFormat.createSuccessResponse(null, "Company registered successfully");
    }

    @Override
    public ResponseFormat companyList(String searchName, String orderBy, int first, int max) {

        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            List<CompanyListProjection> companyListProjection = companyRepository.findAllCompaniesAdminPanel(searchName, orderBy, first, max);

            List<CompanyListDTO> companies = companyListProjection.stream()
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

            return ResponseFormat.createSuccessResponse(companies, "Company list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat companyUpdate(String companyGuid, CompanyUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Company company = companyRepository.findByGuid(companyGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

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

        return ResponseFormat.createSuccessResponse(null, "Company updated successfully");
    }

    @Override
    public ResponseFormat companyDelete(String companyGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Company company = companyRepository.findByGuid(companyGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        company.setIsDeleted(true);
        company.setUpdatedAt(ZonedDateTime.now());
        company.setUpdatedBy(admin.getId());

        companyRepository.save(company);

        return ResponseFormat.createSuccessResponse(null, "Company deleted successfully");
    }
}
