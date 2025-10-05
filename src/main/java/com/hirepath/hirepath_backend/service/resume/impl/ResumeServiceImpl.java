package com.hirepath.hirepath_backend.service.resume.impl;

import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.repository.resume.ResumeRepository;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    @Override
    public Resume findByGuid(String guid) {
        try {
            return resumeRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
