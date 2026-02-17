package com.hirepath.hirepath_backend.repository.education;

import com.hirepath.hirepath_backend.model.entity.education.Education;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRepository extends CrudRepository<Education, Long> {
    Optional<Education> findByGuid(String guid);
    List<Education> findAllByUserAndIsDeletedFalse(User user);
}
