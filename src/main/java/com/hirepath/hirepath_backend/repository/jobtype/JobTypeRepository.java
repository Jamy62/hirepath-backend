package com.hirepath.hirepath_backend.repository.jobtype;

import com.hirepath.hirepath_backend.model.entity.jobtype.JobType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTypeRepository extends CrudRepository<JobType, Long> {
}
