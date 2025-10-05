package com.hirepath.hirepath_backend.service.workexperience.impl;

import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;
import com.hirepath.hirepath_backend.repository.workexperience.WorkExperienceRepository;
import com.hirepath.hirepath_backend.service.workexperience.WorkExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;

    @Override
    public WorkExperience findByGuid(String guid) {
        try {
            return workExperienceRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "WorkExperience not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
