package com.hirepath.hirepath_backend.repository.education;

import com.hirepath.hirepath_backend.model.entity.education.Education;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationRepository extends CrudRepository<Education, Long> {
    Optional<Education> findByGuid(String guid);
}
