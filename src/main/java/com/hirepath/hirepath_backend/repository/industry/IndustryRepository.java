package com.hirepath.hirepath_backend.repository.industry;

import com.hirepath.hirepath_backend.model.dto.IndustryListProjection;
import com.hirepath.hirepath_backend.model.entity.industry.Industry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndustryRepository extends CrudRepository<Industry, Long> {
    Optional<Industry> findByGuid(String guid);

    @Query(value = """
            SELECT
                i.name AS name,
                i.description AS description,
                i.guid AS guid,
                i.is_deleted AS isDeleted,
                i.created_at AS createdAt,
                i.updated_at AS updatedAt
            FROM industries i
            WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND i.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN i.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN i.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<IndustryListProjection> findAllIndustriesAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
