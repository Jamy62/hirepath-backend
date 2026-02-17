package com.hirepath.hirepath_backend.repository.skill;

import com.hirepath.hirepath_backend.model.entity.skill.Skill;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {
    Optional<Skill> findByGuid(String guid);
    List<Skill> findAllByUserAndIsDeletedFalse(User user);
}
