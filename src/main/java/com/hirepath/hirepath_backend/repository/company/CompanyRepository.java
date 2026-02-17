package com.hirepath.hirepath_backend.repository.company;

import com.hirepath.hirepath_backend.model.dto.company.CompanyListProjection;
import com.hirepath.hirepath_backend.model.dto.report.JobApplicationSuccessRateProjection;
import com.hirepath.hirepath_backend.model.dto.report.MostPopularCompanyProjection;
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
            AND c.verification_status = :verificationStatus
            ORDER BY
            CASE WHEN :orderBy = 'ASC' THEN c.created_at END ASC,
            CASE WHEN :orderBy = 'DESC' THEN c.created_at END DESC
            LIMIT :max OFFSET :first
            """,
            nativeQuery = true)
    List<CompanyListProjection> findAllCompaniesAdminPanel(@Param("searchName") String searchName,
                                                           @Param("orderBy") String orderBy,
                                                           @Param("first") int first,
                                                           @Param("max") int max,
                                                           @Param("verificationStatus") String verificationStatus);

    @Query(value = """
            SELECT
                c.name AS companyName,
                COUNT(a.id) AS applicationCount,
                c.logo as logo,
                c.website as website
            FROM companies c
            JOIN jobs j ON c.id = j.company_id
            JOIN applications a ON j.id = a.job_id
            WHERE c.is_deleted = 0
            AND j.is_deleted = 0
            AND a.is_deleted = 0
            GROUP BY c.name, c.logo, c.website
            ORDER BY applicationCount DESC
            """, nativeQuery = true)
    List<MostPopularCompanyProjection> findMostPopularCompanies();

    @Query(value = """
            SELECT
                c.name AS companyName,
                (SUM(CASE WHEN a.status = 'ACCEPTED' THEN 1 ELSE 0 END) / COUNT(a.id)) * 100 AS successRate,
                c.logo as logo
            FROM companies c
            JOIN jobs j ON c.id = j.company_id
            JOIN applications a ON j.id = a.job_id
            WHERE c.is_deleted = 0
            AND j.is_deleted = 0
            AND a.is_deleted = 0
            GROUP BY c.name, c.logo
            """, nativeQuery = true)
    List<JobApplicationSuccessRateProjection> findJobApplicationSuccessRates();
}
