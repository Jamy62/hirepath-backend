package com.hirepath.hirepath_backend.service.auth.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.CompanySwitchRequest;
import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.LoginResponse;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.auth.AuthService;
import com.hirepath.hirepath_backend.util.JwtUtil;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;

    public ResponseFormat login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

            if (passwordEncoder.matches(password, user.getPassword())) {
                String systemRole = user.getRole() != null ? user.getRole().getName() : VariableConstant.USER;
                Map<String, String> companyRoleData = new HashMap<>();

                companyUserRepository.findByUserAndIsDeleted(user, false)
                        .ifPresent(companyUsers ->
                                companyUsers.forEach(companyUser -> {
                                    if (companyUser.getRole().getType().equals(Role.RoleType.COMPANY)) {
                                        Company company = companyUser.getCompany();
                                        String companyRole = companyUser.getRole().getName();

                                        companyRoleData.put(company.getGuid(), companyRole);
                                    }
                                }));

                LoginResponse loginResponse = LoginResponse.builder()
                        .token(jwtUtil.generateToken(email, systemRole, companyRoleData.isEmpty() ? null : companyRoleData))
                        .build();

                return ResponseFormat.createSuccessResponse(loginResponse, "Login success");
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void logout(String email) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

            user.setLastLoginAt(ZonedDateTime.now());
            userRepository.save(user);
    }

    public ResponseFormat companyAccess(String companyGuid, String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
            Company company = companyRepository.findByGuid(companyGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
            Optional<CompanyUser> companyUser = companyUserRepository.findByUserAndCompanyAndIsDeleted(user, company, false);

            if (companyUser.isEmpty() || companyUser.get().getRole().getType() != Role.RoleType.COMPANY) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no role in this company");
            }
            String companyRole = companyUser.get().getRole().getName();
            Map<String, String> data = new HashMap<>();
            data.put("companyGuid", companyGuid);
            data.put("companyRole", companyRole);

            return ResponseFormat.createSuccessResponse(data, "Successfully switched to company");
        }
        catch (Exception e) {
            throw e;
        }
    }
}
