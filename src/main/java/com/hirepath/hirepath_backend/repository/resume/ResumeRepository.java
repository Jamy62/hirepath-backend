package com.hirepath.hirepath_backend.repository.resume;

import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends CrudRepository<Resume, Long> {
    Optional<Resume> findByGuid(String guid);
    List<Resume> findAllByUser(User user);
}
