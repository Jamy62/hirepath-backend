package com.hirepath.hirepath_backend.repository.preferredindustry;

import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredIndustryRepository extends CrudRepository<PreferredIndustry, Long> {
}
