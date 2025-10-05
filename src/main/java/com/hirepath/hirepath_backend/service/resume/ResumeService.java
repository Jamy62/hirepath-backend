package com.hirepath.hirepath_backend.service.resume;

import com.hirepath.hirepath_backend.model.entity.resume.Resume;

public interface ResumeService {
    Resume findByGuid(String guid);
}
