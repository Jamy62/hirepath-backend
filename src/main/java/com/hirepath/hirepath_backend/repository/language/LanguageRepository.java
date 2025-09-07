package com.hirepath.hirepath_backend.repository.language;

import com.hirepath.hirepath_backend.model.dto.LanguageListProjection;
import com.hirepath.hirepath_backend.model.entity.language.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
    Optional<Language> findByGuid(String guid);

    @Query(value = """
            SELECT
                l.name AS name,
                l.code AS code,
                l.guid AS guid,
                l.is_deleted AS isDeleted,
                l.created_at AS createdAt,
                l.updated_at AS updatedAt
            FROM languages l
            WHERE (LOWER(l.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND l.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN l.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN l.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<LanguageListProjection> findAllLanguagesAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
