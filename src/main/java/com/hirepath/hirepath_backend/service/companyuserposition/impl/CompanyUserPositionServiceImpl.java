package com.hirepath.hirepath_backend.service.companyuserposition.impl;

import com.hirepath.hirepath_backend.model.dto.companyuserposition.CompanyUserPositionListDTO;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.model.entity.position.Position;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.companyuserposition.CompanyUserPositionListRequest;
import com.hirepath.hirepath_backend.model.request.companyuserposition.PositionListRequest;
import com.hirepath.hirepath_backend.repository.companyuserposition.CompanyUserPositionRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.companyuser.CompanyUserService;
import com.hirepath.hirepath_backend.service.companyuserposition.CompanyUserPositionService;
import com.hirepath.hirepath_backend.service.position.PositionService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyUserPositionServiceImpl implements CompanyUserPositionService {

    private final CompanyUserPositionRepository companyUserPositionRepository;
    private final UserService userService;
    private final CompanyService companyService;
    private final CompanyUserService companyUserService;
    private final PositionService positionService;

    @Override
    public CompanyUserPosition findByGuid(String guid) {
        try {
            return companyUserPositionRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CompanyUserPosition not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void companyUserPositionsAssign(String companyUserGuid, PositionListRequest request, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);
            CompanyUser companyUser = companyUserService.findByGuid(companyUserGuid);

            if (!companyUser.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to assign positions to this user");
            }

            for (String positionGuid : request.getPositionGuids()) {
                Position position = positionService.findByGuid(positionGuid);

                if (!position.getCompany().equals(company)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to assign this position");
                }

                CompanyUserPosition companyUserPosition = CompanyUserPosition.builder()
                        .companyUser(companyUser)
                        .position(position)
                        .guid(UUID.randomUUID().toString())
                        .isDeleted(false)
                        .createdAt(ZonedDateTime.now())
                        .createdBy(user.getId())
                        .build();

                companyUserPositionRepository.save(companyUserPosition);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void companyUserPositionsDelete(CompanyUserPositionListRequest request, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);

            for (String companyUserPositionGuid : request.getCompanyUserPositionGuids()) {
                CompanyUserPosition companyUserPosition = findByGuid(companyUserPositionGuid);

                if (!companyUserPosition.getCompanyUser().getCompany().equals(company)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this user position");
                }

                companyUserPosition.setIsDeleted(true);
                companyUserPosition.setUpdatedAt(ZonedDateTime.now());
                companyUserPosition.setUpdatedBy(user.getId());

                companyUserPositionRepository.save(companyUserPosition);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CompanyUserPositionListDTO> companyUserPositionList(String companyUserGuid, String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            CompanyUser companyUser = companyUserService.findByGuid(companyUserGuid);

            if (!companyUser.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this user's position");
            }

            List<CompanyUserPosition> companyUserPositions = companyUserPositionRepository.findAllByCompanyUserAndIsDeletedFalse(companyUser);

            return companyUserPositions.stream()
                    .map(c -> CompanyUserPositionListDTO.builder()
                            .name(c.getPosition().getName())
                            .guid(c.getGuid())
                            .positionGuid(c.getPosition().getGuid())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }
}
