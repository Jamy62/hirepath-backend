package com.hirepath.hirepath_backend.repository.jobfunction;

import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobFunctionRepository extends CrudRepository<JobFunction, Long> {
}
