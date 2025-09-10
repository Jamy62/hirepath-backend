package com.hirepath.hirepath_backend.service.township.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.township.TownshipListDTO;
import com.hirepath.hirepath_backend.model.dto.township.TownshipListProjection;
import com.hirepath.hirepath_backend.model.entity.province.Province;
import com.hirepath.hirepath_backend.model.entity.township.Township;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.township.TownshipCreateRequest;
import com.hirepath.hirepath_backend.model.request.township.TownshipUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.province.ProvinceRepository;
import com.hirepath.hirepath_backend.repository.township.TownshipRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.township.TownshipService;
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
public class TownshipServiceImpl implements TownshipService {

    private final TownshipRepository townshipRepository;
    private final UserRepository userRepository;
    private final ProvinceRepository provinceRepository;

    @Override
    public ResponseFormat townshipCreate(TownshipCreateRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Province province = provinceRepository.findByGuid(request.getProvinceGuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Province not found"));

        Township township = Township.builder()
                .name(request.getName())
                .province(province)
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(admin.getId())
                .build();

        townshipRepository.save(township);

        return ResponseFormat.createSuccessResponse(null, "Township created successfully");
    }

    @Override
    public ResponseFormat townshipList(String searchName, String orderBy, int first, int max) {
        if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
            // This method will need to be created in TownshipRepository
            List<TownshipListProjection> townshipListProjections = townshipRepository.findAllTownshipsAdminPanel(searchName, orderBy, first, max);

            List<TownshipListDTO> townships = townshipListProjections.stream()
                    .map(p -> TownshipListDTO.builder()
                            .name(p.getName())
                            .provinceName(p.getProvinceName())
                            .guid(p.getGuid())
                            .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .toList();

            return ResponseFormat.createSuccessResponse(townships, "Township list retrieved successfully");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
    }

    @Override
    public ResponseFormat townshipUpdate(String townshipGuid, TownshipUpdateRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Township township = townshipRepository.findByGuid(townshipGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Township not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            township.setName(request.getName());
        }
        if (request.getProvinceGuid() != null && !request.getProvinceGuid().isBlank()) {
            Province province = provinceRepository.findByGuid(request.getProvinceGuid())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Province not found"));
            township.setProvince(province);
        }

        township.setUpdatedAt(ZonedDateTime.now());
        township.setUpdatedBy(admin.getId());

        townshipRepository.save(township);

        return ResponseFormat.createSuccessResponse(null, "Township updated successfully");
    }

    @Override
    public ResponseFormat townshipDelete(String townshipGuid, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

        Township township = townshipRepository.findByGuid(townshipGuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Township not found"));

        township.setIsDeleted(true);
        township.setUpdatedAt(ZonedDateTime.now());
        township.setUpdatedBy(admin.getId());

        townshipRepository.save(township);

        return ResponseFormat.createSuccessResponse(null, "Township deleted successfully");
    }
}
