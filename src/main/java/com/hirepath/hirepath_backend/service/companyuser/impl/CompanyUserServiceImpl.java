package com.hirepath.hirepath_backend.service.companyuser.impl;

import com.hirepath.hirepath_backend.model.dto.companyuser.CompanyUserListDTO;
import com.hirepath.hirepath_backend.model.dto.companyuser.CompanyUserPositionsDTO;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.companyuser.AssignCompanyRoleRequest;
import com.hirepath.hirepath_backend.model.request.companyuser.ReassignCompanyRoleRequest;
import com.hirepath.hirepath_backend.repository.application.ApplicationRepository;
import com.hirepath.hirepath_backend.repository.companyuser.CompanyUserRepository;
import com.hirepath.hirepath_backend.repository.companyuserposition.CompanyUserPositionRepository;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.companyuser.CompanyUserService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CompanyUserServiceImpl implements CompanyUserService {
    private final CompanyService companyService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final CompanyUserRepository companyUserRepository;
    private final CompanyUserPositionRepository companyUserPositionRepository;
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;

    @Override
    public CompanyUser findByGuid(String guid) {
        try {
            return companyUserRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company user not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    public void assignCompanyRole(AssignCompanyRoleRequest request, String email) {
        try {
            Company company = companyService.findByGuid(request.getCompanyGuid());
            User user = userService.findByGuid(request.getUserGuid());
            Role newRole = roleRepository.findByGuid(request.getRoleGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
            User assigner = userService.findByEmail(email);

            Optional<CompanyUser> existingCompanyUserOpt = companyUserRepository.findByUserAndCompanyIncludingDeleted(user.getId(), company.getId());

            if (existingCompanyUserOpt.isPresent()) {
                CompanyUser existingCompanyUser = existingCompanyUserOpt.get();

                if (!existingCompanyUser.getIsDeleted() && existingCompanyUser.getRole().equals(newRole)) {
                    return;
                }

                existingCompanyUser.setRole(newRole);
                existingCompanyUser.setIsDeleted(false);
                existingCompanyUser.setUpdatedAt(ZonedDateTime.now());
                existingCompanyUser.setUpdatedBy(assigner.getId());
                companyUserRepository.save(existingCompanyUser);
            } else {
                CompanyUser companyUser = CompanyUser.builder()
                        .company(company)
                        .user(user)
                        .role(newRole)
                        .guid(UUID.randomUUID().toString())
                        .isDeleted(false)
                        .createdAt(ZonedDateTime.now())
                        .createdBy(assigner.getId())
                        .build();
                companyUserRepository.save(companyUser);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void reassignCompanyRole(String companyUserGuid, ReassignCompanyRoleRequest request, String email, String companyGuid) {
        try {
            User requester = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);
            CompanyUser targetCompanyUser = findByGuid(companyUserGuid);
            Role newRole = roleRepository.findByGuid(request.getNewRoleGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "New role not found"));

            if (!targetCompanyUser.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Company user does not belong to this company");
            }

            boolean requesterIsAdmin = requester.getRole().getName().equals("ADMIN");
            boolean requesterIsCompanyOwner = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(requester, company)
                    .map(cu -> cu.getRole().getName().equals("COMPANY_OWNER"))
                    .orElse(false);

            if (requesterIsAdmin && newRole.getName().equals("COMPANY_OWNER")) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admins cannot assign the COMPANY_OWNER role.");
            }

            if (targetCompanyUser.getRole().getName().equals("COMPANY_OWNER") && !requesterIsCompanyOwner) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only a COMPANY_OWNER can reassign another COMPANY_OWNER.");
            }

            if (targetCompanyUser.getRole().getName().equals("COMPANY_OWNER") && newRole.getName().equals("COMPANY_OWNER")) {
                Role companyAdminRole = roleRepository.findByName("COMPANY_ADMIN")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "COMPANY_ADMIN role not found"));
                CompanyUser currentOwnerCompanyUser = companyUserRepository.findByUserAndCompanyAndIsDeletedFalse(requester, company)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Requester's company user record not found."));
                currentOwnerCompanyUser.setRole(companyAdminRole);
                currentOwnerCompanyUser.setUpdatedAt(ZonedDateTime.now());
                currentOwnerCompanyUser.setUpdatedBy(requester.getId());
                companyUserRepository.save(currentOwnerCompanyUser);
            }

            targetCompanyUser.setRole(newRole);
            targetCompanyUser.setUpdatedAt(ZonedDateTime.now());
            targetCompanyUser.setUpdatedBy(requester.getId());
            companyUserRepository.save(targetCompanyUser);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CompanyUserListDTO> employeeList(String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            List<CompanyUser> companyUserList = companyUserRepository.findAllByCompanyAndIsDeletedFalse(company);

            return companyUserList.stream()
                    .map(companyUser -> {
                        List<CompanyUserPosition> positionList = companyUserPositionRepository.findAllByCompanyUserAndIsDeletedFalse(companyUser);

                        List<CompanyUserPositionsDTO> positionsDTOList = positionList.stream()
                                .map(p -> CompanyUserPositionsDTO.builder()
                                        .name(p.getPosition().getName())
                                        .guid(p.getPosition().getGuid())
                                        .build())
                                .toList();

                        return CompanyUserListDTO.builder()
                                .name(companyUser.getUser().getName())
                                .email(companyUser.getUser().getEmail())
                                .guid(companyUser.getGuid())
                                .userGuid(companyUser.getUser().getGuid())
                                .role(companyUser.getRole().getName())
                                .profilePicture(companyUser.getUser().getProfile())
                                .joinedDate(companyUser.getCreatedAt() != null ? companyUser.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .positions(positionsDTOList)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void employeeDelete(String companyUserGuid, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);
            CompanyUser companyUser = findByGuid(companyUserGuid);

            if (!companyUser.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this user");
            }

            List<Application> applications = applicationService.findAllByUserAndCompanyAndStatus(companyUser.getUser(), company, "ACCEPTED");
            for (Application application : applications) {
                application.setStatus("REJECTED");
                application.setUpdatedAt(ZonedDateTime.now());
                application.setUpdatedBy(user.getId());
                applicationRepository.save(application);
            }

            List<CompanyUserPosition> positions = companyUserPositionRepository.findAllByCompanyUserAndIsDeletedFalse(companyUser);
            for (CompanyUserPosition position : positions) {
                position.setIsDeleted(true);
                position.setUpdatedAt(ZonedDateTime.now());
                position.setUpdatedBy(user.getId());
                companyUserPositionRepository.save(position);
            }

            companyUser.setIsDeleted(true);
            companyUser.setUpdatedAt(ZonedDateTime.now());
            companyUser.setUpdatedBy(user.getId());

            companyUserRepository.save(companyUser);
        } catch (Exception e) {
            throw e;
        }
    }
}
