package com.hirepath.hirepath_backend.service.skill;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;

public interface SkillService {
    Skill findByGuid(String guid);
}
