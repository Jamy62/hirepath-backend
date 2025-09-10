package com.hirepath.hirepath_backend.repository.experiencelevel;

import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListProjection;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperienceLevelRepository extends CrudRepository<ExperienceLevel, Long> {
    Optional<ExperienceLevel> findByGuid(String guid);

    @Query(value = """
            SELECT
                el.name AS name,
                el.description AS description,
                el.guid AS guid,
                el.is_deleted AS isDeleted,
                el.created_at AS createdAt,
                el.updated_at AS updatedAt
            FROM experience_levels el
            WHERE (LOWER(el.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND el.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN el.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN el.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<ExperienceLevelListProjection> findAllExperienceLevelsAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
