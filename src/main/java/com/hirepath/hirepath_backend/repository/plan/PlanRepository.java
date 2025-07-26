package com.hirepath.hirepath_backend.repository.plan;

import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
}
