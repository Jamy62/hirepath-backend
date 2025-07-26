package com.hirepath.hirepath_backend.repository.industry;

import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends CrudRepository<Industry, Long> {
}
