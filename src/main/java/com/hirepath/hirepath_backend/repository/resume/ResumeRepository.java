package com.hirepath.hirepath_backend.repository.resume;

import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends CrudRepository<Resume, Long> {
}
