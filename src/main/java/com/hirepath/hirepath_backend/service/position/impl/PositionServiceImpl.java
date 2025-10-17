package com.hirepath.hirepath_backend.service.position.impl;

import com.hirepath.hirepath_backend.model.dto.position.PositionListDTO;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.position.Position;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.position.PositionCreateRequest;
import com.hirepath.hirepath_backend.repository.position.PositionRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
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
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final CompanyService companyService;
    private final UserService userService;

    @Override
    public Position findByGuid(String guid) {
        try {
            return positionRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void positionCreate(PositionCreateRequest request, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);

            Position position = Position.builder()
                    .company(company)
                    .name(request.getName())
                    .description(request.getDescription())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            positionRepository.save(position);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<PositionListDTO> positionList(String companyGuid) {
        try {
            Company company = companyService.findByGuid(companyGuid);
            List<Position> positionList = positionRepository.findAllByCompanyAndIsDeletedFalse(company);

            return positionList.stream()
                    .map(position -> PositionListDTO.builder()
                            .name(position.getName())
                            .description(position.getDescription())
                            .guid(position.getGuid())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void positionDelete(String positionGuid, String email, String companyGuid) {
        try {
            User user = userService.findByEmail(email);
            Company company = companyService.findByGuid(companyGuid);
            Position position = findByGuid(positionGuid);

            if (!position.getCompany().equals(company)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this position");
            }

            position.setIsDeleted(true);
            position.setUpdatedAt(ZonedDateTime.now());
            position.setUpdatedBy(user.getId());

            positionRepository.save(position);
        } catch (Exception e) {
            throw e;
        }
    }
}
