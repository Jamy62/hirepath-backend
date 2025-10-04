package com.hirepath.hirepath_backend.repository.plan;

import com.hirepath.hirepath_backend.model.dto.plan.PlanListProjection;
import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
    Optional<Plan> findByGuid(String guid);

    @Query(value = """
            SELECT
                p.name as name,
                p.description as description,
                p.price as price,
                p.duration_days as durationDays,
                p.features as features,
                p.guid as guid,
                p.created_at as createdAt,
                p.updated_at as updatedAt
            FROM plans p
            WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND p.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN p.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN p.created_at END DESC
            LIMIT :max OFFSET :first
            """,
            nativeQuery = true)
    List<PlanListProjection> findAllPlansAdminPanel(@Param("searchName") String searchName,
                                                    @Param("orderBy") String orderBy,
                                                    @Param("first") int first,
                                                    @Param("max") int max);
}
