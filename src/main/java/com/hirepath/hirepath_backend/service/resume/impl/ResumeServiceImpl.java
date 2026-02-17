package com.hirepath.hirepath_backend.service.resume.impl;

import com.hirepath.hirepath_backend.model.dto.resume.ResumeListDTO;
import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.repository.resume.ResumeRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserService userService;

    @Override
    public Resume findByGuid(String guid) {
        try {
            return resumeRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<ResumeListDTO> resumeList(String email) {
        try {
            User user = userService.findByEmail(email);

            return resumeRepository.findAllByUser(user).stream()
                    .map(resume -> ResumeListDTO.builder()
                            .name(resume.getName())
                            .filePath(resume.getFilePath())
                            .guid(resume.getGuid())
                            .createdAt(resume.getCreatedAt() != null ? resume.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .updatedAt(resume.getUpdatedAt() != null ? resume.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }
}
