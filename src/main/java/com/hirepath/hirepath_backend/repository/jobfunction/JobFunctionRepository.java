package com.hirepath.hirepath_backend.repository.jobfunction;

import com.hirepath.hirepath_backend.model.dto.jobfunction.JobFunctionListProjection;
import com.hirepath.hirepath_backend.model.entity.jobfunction.JobFunction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobFunctionRepository extends CrudRepository<JobFunction, Long> {
    Optional<JobFunction> findByGuid(String guid);

    @Query(value = """
            SELECT
                jf.name AS name,
                jf.description AS description,
                jf.guid AS guid,
                jf.is_deleted AS isDeleted,
                jf.created_at AS createdAt,
                jf.updated_at AS updatedAt
            FROM job_function jf
            WHERE (LOWER(jf.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND jf.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN jf.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN jf.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<JobFunctionListProjection> findAllJobFunctionsAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
