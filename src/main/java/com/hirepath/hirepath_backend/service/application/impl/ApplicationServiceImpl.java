package com.hirepath.hirepath_backend.service.application.impl;

import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.repository.application.ApplicationRepository;
import com.hirepath.hirepath_backend.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application findByGuid(String guid) {
        try {
            return applicationRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
