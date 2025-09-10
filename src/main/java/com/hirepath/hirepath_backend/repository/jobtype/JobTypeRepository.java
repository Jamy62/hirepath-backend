package com.hirepath.hirepath_backend.repository.jobtype;

import com.hirepath.hirepath_backend.model.dto.jobtype.JobTypeListProjection;
import com.hirepath.hirepath_backend.model.entity.jobtype.JobType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobTypeRepository extends CrudRepository<JobType, Long> {
    Optional<JobType> findByGuid(String guid);

    @Query(value = """
            SELECT
                jt.name AS name,
                jt.description AS description,
                jt.guid AS guid,
                jt.is_deleted AS isDeleted,
                jt.created_at AS createdAt,
                jt.updated_at AS updatedAt
            FROM job_types jt
            WHERE (LOWER(jt.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND jt.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN jt.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN jt.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<JobTypeListProjection> findAllJobTypesAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
