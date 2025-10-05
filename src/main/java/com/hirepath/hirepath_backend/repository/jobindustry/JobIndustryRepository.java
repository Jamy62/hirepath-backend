package com.hirepath.hirepath_backend.repository.jobindustry;

import com.hirepath.hirepath_backend.model.entity.jobindustry.JobIndustry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobIndustryRepository extends CrudRepository<JobIndustry, Long> {
    Optional<JobIndustry> findByGuid(String guid);
}
