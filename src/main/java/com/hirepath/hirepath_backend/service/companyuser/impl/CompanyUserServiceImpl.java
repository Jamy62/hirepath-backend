package com.hirepath.hirepath_backend.service.companyuser.impl;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.companyuser.CompanyUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyUserServiceImpl implements CompanyUserService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyUserRepository companyUserRepository;

    public ResponseFormat assignCompanyRole(AssignCompanyRoleRequest request, String email) {
        try {
            Company company = companyRepository.findByGuid(request.getCompanyGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
            User user = userRepository.findByGuid(request.getUserGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
            Role role = roleRepository.findByGuid(request.getRoleGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            User assigner = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assigner not found"));

            if (user.getRole().getName().equals("COMPANY_ADMIN") && role.getName().equals("COMPANY_OWNER")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot assign Company Owner as an Admin");
            }
            else if (role.getName().equals("COMPANY_OWNER")) {
                assigner.setRole(roleRepository.findByName("COMPANY_ADMIN")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")));
            }

            CompanyUser companyUser = CompanyUser.builder()
                    .company(company)
                    .user(user)
                    .role(role)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(assigner.getId())
                    .build();
            companyUserRepository.save(companyUser);

            return ResponseFormat.createSuccessResponse(null, "Company role assigned successfully");
        } catch (Exception e) {
            throw e;
        }
    }
}
