package com.hirepath.hirepath_backend.service.skill.impl;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.repository.skill.SkillRepository;
import com.hirepath.hirepath_backend.service.skill.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Override
    public Skill findByGuid(String guid) {
        try {
            return skillRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
