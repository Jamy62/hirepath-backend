package com.hirepath.hirepath_backend.repository.job;

import com.hirepath.hirepath_backend.model.entity.job.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends CrudRepository<Job, Long> {
    Optional<Job> findByGuid(String guid);
}
