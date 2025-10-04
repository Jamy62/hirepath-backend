package com.hirepath.hirepath_backend.service.province.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.province.ProvinceListDTO;
import com.hirepath.hirepath_backend.model.dto.province.ProvinceListProjection;
import com.hirepath.hirepath_backend.model.entity.province.Province;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.province.ProvinceCreateRequest;
import com.hirepath.hirepath_backend.model.request.province.ProvinceUpdateRequest;
import com.hirepath.hirepath_backend.repository.province.ProvinceRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.province.ProvinceService;
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
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final UserRepository userRepository;

    @Override
    public void provinceCreate(ProvinceCreateRequest request, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Province province = Province.builder()
                    .name(request.getName())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(admin.getId())
                    .build();

            provinceRepository.save(province);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ProvinceListDTO> provinceList(String searchName, String orderBy, int first, int max) {
        try {
            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<ProvinceListProjection> provinceListProjections = provinceRepository.findAllProvincesAdminPanel(searchName, orderBy, first, max);

                return provinceListProjections.stream()
                        .map(p -> ProvinceListDTO.builder()
                                .name(p.getName())
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
    public void provinceUpdate(String provinceGuid, ProvinceUpdateRequest request, String email) {
        try {
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Province province = provinceRepository.findByGuid(provinceGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Province not found"));

            if (request.getName() != null && !request.getName().isBlank()) {
                province.setName(request.getName());
            }

            province.setUpdatedAt(ZonedDateTime.now());
            province.setUpdatedBy(admin.getId());

            provinceRepository.save(province);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void provinceDelete(String provinceGuid, String adminEmail) {
        try {
            User admin = userRepository.findByEmail(adminEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin user not found"));

            Province province = provinceRepository.findByGuid(provinceGuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Province not found"));

            province.setIsDeleted(true);
            province.setUpdatedAt(ZonedDateTime.now());
            province.setUpdatedBy(admin.getId());

            provinceRepository.save(province);
        } catch (Exception e) {
            throw e;
        }
    }
}
