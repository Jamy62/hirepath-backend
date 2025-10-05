package com.hirepath.hirepath_backend.service.education.impl;

import com.hirepath.hirepath_backend.model.entity.education.Education;
import com.hirepath.hirepath_backend.repository.education.EducationRepository;
import com.hirepath.hirepath_backend.service.education.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;

    @Override
    public Education findByGuid(String guid) {
        try {
            return educationRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Education not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
