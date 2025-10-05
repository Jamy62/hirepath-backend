package com.hirepath.hirepath_backend.service.preferredindustry.impl;

import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import com.hirepath.hirepath_backend.repository.preferredindustry.PreferredIndustryRepository;
import com.hirepath.hirepath_backend.service.preferredindustry.PreferredIndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PreferredIndustryServiceImpl implements PreferredIndustryService {

    private final PreferredIndustryRepository preferredIndustryRepository;

    @Override
    public PreferredIndustry findByGuid(String guid) {
        try {
            return preferredIndustryRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PreferredIndustry not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
