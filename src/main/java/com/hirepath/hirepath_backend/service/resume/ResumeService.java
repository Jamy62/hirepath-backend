package com.hirepath.hirepath_backend.service.resume;

import com.hirepath.hirepath_backend.model.dto.resume.ResumeListDTO;
import com.hirepath.hirepath_backend.model.entity.resume.Resume;

import java.util.List;

public interface ResumeService {
    Resume findByGuid(String guid);
    List<ResumeListDTO> resumeList(String email);
}
