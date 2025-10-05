package com.hirepath.hirepath_backend.service.jobindustry.impl;

import com.hirepath.hirepath_backend.model.entity.jobindustry.JobIndustry;
import com.hirepath.hirepath_backend.repository.jobindustry.JobIndustryRepository;
import com.hirepath.hirepath_backend.service.jobindustry.JobIndustryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class JobIndustryServiceImpl implements JobIndustryService {

    private final JobIndustryRepository jobIndustryRepository;

    @Override
    public JobIndustry findByGuid(String guid) {
        try {
            return jobIndustryRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JobIndustry not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
