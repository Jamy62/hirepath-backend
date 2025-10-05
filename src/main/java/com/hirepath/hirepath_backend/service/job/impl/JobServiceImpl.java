package com.hirepath.hirepath_backend.service.job.impl;

import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.repository.job.JobRepository;
import com.hirepath.hirepath_backend.service.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public Job findByGuid(String guid) {
        try {
            return jobRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
