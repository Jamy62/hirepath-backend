package com.hirepath.hirepath_backend.service.skill.impl;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.skill.SkillCreateRequest;
import com.hirepath.hirepath_backend.repository.skill.SkillRepository;
import com.hirepath.hirepath_backend.service.skill.SkillService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final UserService userservice;

    @Override
    public Skill findByGuid(String guid) {
        try {
            return skillRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void skillCreate(SkillCreateRequest request, String email) {
        try {
            User user = userservice.findByEmail(email);
            Skill skill = Skill.builder()
                    .user(user)
                    .name(request.getName())
                    .proficiency(request.getProficiency())
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(user.getId())
                    .build();

            skillRepository.save(skill);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void skillDelete(String skillGuid, String email) {
        try {
            User user = userservice.findByEmail(email);
            Skill skill = findByGuid(skillGuid);
            if (!user.equals(skill.getUser())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this item");
            }

            skill.setIsDeleted(true);
            skill.setUpdatedAt(ZonedDateTime.now());
            skill.setUpdatedBy(user.getId());

            skillRepository.save(skill);
        } catch (Exception e) {
            throw e;
        }
    }
}
