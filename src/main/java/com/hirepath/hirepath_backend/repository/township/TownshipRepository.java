package com.hirepath.hirepath_backend.repository.township;

import com.hirepath.hirepath_backend.model.dto.province.ProvinceListProjection;
import com.hirepath.hirepath_backend.model.dto.township.TownshipListProjection;
import com.hirepath.hirepath_backend.model.entity.township.Township;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TownshipRepository extends CrudRepository<Township, Long> {
    Optional<Township> findByGuid(String guid);

    @Query(value = """
            SELECT
                t.name AS name,
                p.name AS provinceName,
                t.guid AS guid,
                t.is_deleted AS isDeleted,
                t.created_at AS createdAt,
                t.updated_at AS updatedAt
            FROM townships t
            INNER JOIN provinces p ON t.province_id = p.id
            WHERE (LOWER(t.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND t.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN t.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN t.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<TownshipListProjection> findAllTownshipsAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
