package com.hirepath.hirepath_backend.repository.company;

import com.hirepath.hirepath_backend.model.dto.company.CompanyListProjection;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByGuid(String guid);

    @Query(value = """
            SELECT
                c.name as name,
                c.logo as logo,
                c.email as email,
                c.phone as phone,
                c.verification_status as verificationStatus,
                c.guid as guid,
                c.created_at as createdAt,
                c.updated_at as updatedAt
            FROM companies c
            WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', COALESCE(:searchName, ''), '%')) OR :searchName IS NULL)
            AND c.is_deleted = 0
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN c.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN c.created_at END DESC
            LIMIT :max OFFSET :first
            """,
            nativeQuery = true)
    List<CompanyListProjection> findAllCompaniesAdminPanel(@Param("searchName") String searchName,
                                                           @Param("orderBy") String orderBy,
                                                           @Param("first") int first,
                                                           @Param("max") int max);
}
