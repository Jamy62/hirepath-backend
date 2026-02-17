package com.hirepath.hirepath_backend.repository.province;

import com.hirepath.hirepath_backend.model.dto.province.ProvinceListProjection;
import com.hirepath.hirepath_backend.model.entity.province.Province;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProvinceRepository extends CrudRepository<Province, Long> {
    Optional<Province> findByGuid(String guid);

    @Query(value = """
            SELECT
                p.name AS name,
                p.guid AS guid,
                p.is_deleted AS isDeleted,
                p.created_at AS createdAt,
                p.updated_at AS updatedAt
            FROM provinces p
            WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND p.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN p.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN p.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<ProvinceListProjection> findAllProvincesAdminPanel(
            @Param("searchName") String searchName,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
