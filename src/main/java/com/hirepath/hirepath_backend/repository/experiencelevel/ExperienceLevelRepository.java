package com.hirepath.hirepath_backend.repository.experiencelevel;

import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceLevelRepository extends CrudRepository<ExperienceLevel, Long> {
}
