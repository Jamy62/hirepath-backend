package com.hirepath.hirepath_backend.service.skill;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.model.request.skill.SkillCreateRequest;

public interface SkillService {
    Skill findByGuid(String guid);
    void skillCreate(SkillCreateRequest request, String email);
    void skillDelete(String skillGuid, String email);
}
