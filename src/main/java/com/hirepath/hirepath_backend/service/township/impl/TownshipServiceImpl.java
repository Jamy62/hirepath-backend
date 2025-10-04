package com.hirepath.hirepath_backend.service.township.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.township.TownshipListDTO;
import com.hirepath.hirepath_backend.model.dto.township.TownshipListProjection;
import com.hirepath.hirepath_backend.model.entity.province.Province;
import com.hirepath.hirepath_backend.model.entity.township.Township;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.township.TownshipCreateRequest;
import com.hirepath.hirepath_backend.model.request.township.TownshipUpdateRequest;
import com.hirepath.hirepath_backend.repository.township.TownshipRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.province.ProvinceService;
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
    private final ProvinceService provinceService;

    @Override
    public Township findByGuid(String guid) {
        try {
            return townshipRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Township not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void townshipCreate(TownshipCreateRequest request, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Province province = provinceService.findByGuid(request.getProvinceGuid());

            Township township = Township.builder()
                    .name(request.getName())
                    .province(province)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            townshipRepository.save(township);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TownshipListDTO> townshipList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<TownshipListProjection> townshipListProjections = townshipRepository.findAllTownshipsAdminPanel(searchName, orderBy, first, max);

                return townshipListProjections.stream()
                        .map(p -> TownshipListDTO.builder()
                                .name(p.getName())
                                .provinceName(p.getProvinceName())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Please enter either ASC or DESC for orderBy");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void townshipUpdate(String townshipGuid, TownshipUpdateRequest request, String email) {
        try {
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Township township = findByGuid(townshipGuid);

            if (request.getName() != null && !request.getName().isBlank()) {
                township.setName(request.getName());
            }
            if (request.getProvinceGuid() != null && !request.getProvinceGuid().isBlank()) {
                Province province = provinceService.findByGuid(request.getProvinceGuid());
                township.setProvince(province);
            }

            township.setUpdatedAt(ZonedDateTime.now());
            township.setUpdatedBy(admin.getId());

            townshipRepository.save(township);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void townshipDelete(String townshipGuid, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Township township = findByGuid(townshipGuid);

            township.setIsDeleted(true);
            township.setUpdatedAt(ZonedDateTime.now());
            township.setUpdatedBy(admin.getId());

            townshipRepository.save(township);
        } catch (Exception e) {
            throw e;
        }
    }
}
