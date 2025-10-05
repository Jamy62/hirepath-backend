package com.hirepath.hirepath_backend.service.companyuserposition.impl;

import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.repository.companyuserposition.CompanyUserPositionRepository;
import com.hirepath.hirepath_backend.service.companyuserposition.CompanyUserPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CompanyUserPositionServiceImpl implements CompanyUserPositionService {

    private final CompanyUserPositionRepository companyUserPositionRepository;

    @Override
    public CompanyUserPosition findByGuid(String guid) {
        try {
            return companyUserPositionRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CompanyUserPosition not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
