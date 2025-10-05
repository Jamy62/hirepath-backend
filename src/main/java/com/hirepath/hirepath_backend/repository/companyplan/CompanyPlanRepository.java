package com.hirepath.hirepath_backend.repository.companyplan;

import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyPlanRepository extends CrudRepository<CompanyPlan, Long> {
    Optional<CompanyPlan> findByGuid(String guid);
}
