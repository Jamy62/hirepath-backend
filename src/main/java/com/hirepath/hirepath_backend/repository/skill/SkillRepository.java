package com.hirepath.hirepath_backend.repository.skill;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {
}
