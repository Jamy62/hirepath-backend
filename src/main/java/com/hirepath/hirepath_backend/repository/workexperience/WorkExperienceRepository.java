package com.hirepath.hirepath_backend.repository.workexperience;

import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.entity.workexperience.WorkExperience;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkExperienceRepository extends CrudRepository<WorkExperience, Long> {
    Optional<WorkExperience> findByGuid(String guid);
    List<WorkExperience> findAllByUserAndIsDeletedFalse(User user);
}
