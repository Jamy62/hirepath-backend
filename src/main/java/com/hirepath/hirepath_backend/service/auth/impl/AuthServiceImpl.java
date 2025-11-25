package com.hirepath.hirepath_backend.service.auth.impl;

import com.hirepath.hirepath_backend.model.dto.company.CompanyDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.response.LoginResponse;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.auth.AuthService;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.user.UserService;
import com.hirepath.hirepath_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CompanyService companyService;
    private final CompanyUserRepository companyUserRepository;

    public LoginResponse login(String email, String password) {
        try {
            User user = userService.findByEmail(email);

            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateSystemToken(user);
                UserDetailDTO userDetail = userService.userDetail(user.getGuid());
                return LoginResponse.builder()
                        .token(token)
                        .user(userDetail)
                        .role("USER")
                        .build();
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        } catch (Exception e) {
            throw e;
        }
    }

    public void logout(String email) {
        try {
            User user = userService.findByEmail(email);

            user.setLastLoginAt(ZonedDateTime.now());
            userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    public LoginResponse companyAccess(String companyGuid, String email) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);
            Optional<CompanyUser> companyUser = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(user, company);

            if (companyUser.isEmpty() || companyUser.get().getRole().getType() != Role.RoleType.COMPANY) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no role in this company");
            }

            String companyRole = companyUser.get().getRole().getName();
            String token = jwtUtil.generateCompanyToken(user, companyGuid, companyRole);
            CompanyDetailDTO companyDetail = companyService.companyDetail(companyGuid);

            return LoginResponse.builder()
                    .token(token)
                    .company(companyDetail)
                    .role("COMPANY")
                    .companyRole(companyRole)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public LoginResponse companySwitchBack(String email) {
        try {
            User user = userService.findByEmail(email);
            String token = jwtUtil.generateSystemToken(user);
            UserDetailDTO userDetail = userService.userDetail(user.getGuid());
            return LoginResponse.builder()
                    .token(token)
                    .user(userDetail)
                    .build();
        } catch (Exception e) {
            throw e;
        }
    }
}
