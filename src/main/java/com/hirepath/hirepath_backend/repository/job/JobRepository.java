package com.hirepath.hirepath_backend.repository.job;

import com.hirepath.hirepath_backend.model.dto.job.JobDetailProjection;
import com.hirepath.hirepath_backend.model.dto.job.JobListProjection;
import com.hirepath.hirepath_backend.model.dto.report.MostPopularJobProjection;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByGuid(String guid);

    @Query(value = """
            SELECT
                j.guid AS guid,
                j.title AS title,
                j.min_salary AS minSalary,
                j.max_salary AS maxSalary,
                t.name AS location,
                c.name AS companyName,
                c.logo AS companyLogo,
                jt.name AS jobType,
                el.name AS experienceLevel,
                j.created_at AS createdAt,
                (CASE
                    WHEN :currentUserId IS NULL THEN 0
                    ELSE EXISTS (
                        SELECT 1 FROM applications a
                        WHERE a.job_id = j.id
                        AND a.user_id = :currentUserId
                        AND a.status != 'REJECTED'
                        AND a.is_deleted = 1
                    )
                END) AS isApplied,
                (CASE
                    WHEN :currentUserId IS NULL THEN 0
                    ELSE EXISTS (
                        SELECT 1 FROM company_user cu
                        WHERE cu.company_id = j.company_id
                        AND cu.user_id = :currentUserId
                        AND cu.is_deleted = 0
                    )
                END) AS isEmployed
            FROM
                jobs j
            JOIN
                companies c ON j.company_id = c.id
            LEFT JOIN
                townships t ON j.township_id = t.id
            LEFT JOIN
                provinces p ON t.province_id = p.id
            LEFT JOIN
                job_types jt ON j.job_type_id = jt.id
            LEFT JOIN
                experience_levels el ON j.experience_level_id = el.id
            LEFT JOIN
                job_industry ji ON j.id = ji.job_id
            LEFT JOIN
                job_function jf ON j.job_function_id = jf.id
            WHERE
                j.is_deleted = 0
                AND j.expire_date >= NOW()
                AND (:searchTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTitle, '%')))
                AND (:companyGuid IS NULL OR c.guid = :companyGuid)
                AND (:provinceGuid IS NULL OR p.guid = :provinceGuid)
                AND (:townshipGuid IS NULL OR t.guid = :townshipGuid)
                AND (:jobTypeGuid IS NULL OR jt.guid = :jobTypeGuid)
                AND (:experienceLevelGuid IS NULL OR el.guid = :experienceLevelGuid)
                AND (:industryGuid IS NULL OR ji.industry_id = (SELECT id FROM industries WHERE guid = :industryGuid))
                AND (:jobFunctionGuid IS NULL OR jf.guid = :jobFunctionGuid)
                AND (:salary IS NULL OR :salary BETWEEN j.min_salary AND j.max_salary)
            GROUP BY j.id
            ORDER BY
                CASE WHEN :orderBy = 'ASC' THEN j.created_at END ASC,
                CASE WHEN :orderBy = 'DESC' THEN j.created_at END DESC
            LIMIT :max OFFSET :first
            """, nativeQuery = true)
    List<JobListProjection> findAllJobsForListView(
            @Param("searchTitle") String searchTitle,
            @Param("companyGuid") String companyGuid,
            @Param("provinceGuid") String provinceGuid,
            @Param("townshipGuid") String townshipGuid,
            @Param("jobTypeGuid") String jobTypeGuid,
            @Param("experienceLevelGuid") String experienceLevelGuid,
            @Param("industryGuid") String industryGuid,
            @Param("jobFunctionGuid") String jobFunctionGuid,
            @Param("salary") Double salary,
            @Param("currentUserId") Long currentUserId,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);

    @Query(value = """
            SELECT
                j.guid AS guid,
                j.title AS title,
                j.description AS description,
                j.requirements AS requirements,
                j.benefits AS benefits,
                j.min_salary AS minSalary,
                j.max_salary AS maxSalary,
                jt.name AS jobType,
                el.name AS experienceLevel,
                t.name AS location,
                c.guid AS companyGuid,
                c.name AS companyName,
                c.logo AS companyLogo,
                j.posted_date AS postedDate,
                j.expire_date AS expireDate,
                (CASE
                    WHEN :userId IS NULL THEN 0
                    ELSE EXISTS (
                        SELECT 1 FROM applications a
                        WHERE a.job_id = j.id
                        AND a.user_id = :userId
                        AND a.status != 'REJECTED'
                        AND a.is_deleted = 1
                    )
                END) AS isApplied,
                (CASE
                    WHEN :userId IS NULL THEN 0
                    ELSE EXISTS (
                        SELECT 1 FROM company_user cu
                        WHERE cu.company_id = j.company_id
                        AND cu.user_id = :userId
                        AND cu.is_deleted = 0
                    )
                END) AS isEmployed
            FROM jobs j
            LEFT JOIN
                job_types jt ON j.job_type_id = jt.id
            LEFT JOIN
                experience_levels el ON j.experience_level_id = el.id
            LEFT JOIN
                townships t ON j.township_id = t.id
            LEFT JOIN
                companies c ON j.company_id = c.id
            WHERE
                j.guid = :jobGuid
            """, nativeQuery = true)
    Optional<JobDetailProjection> findJobDetailByGuid(@Param("jobGuid") String jobGuid, @Param("userId") Long userId);

    @Query(value = """
            SELECT
                j.title AS jobTitle,
                COUNT(a.id) AS applicationCount,
                c.name AS companyName,
                c.logo AS logo,
                c.website AS website
            FROM jobs j
            JOIN applications a ON j.id = a.job_id
            JOIN companies c ON j.company_id = c.id
            WHERE a.application_date >= :startDate
            AND j.is_deleted = 0
            AND a.is_deleted = 0
            AND c.is_deleted = 0
            GROUP BY j.title, c.name, c.logo, c.website
            ORDER BY applicationCount DESC
            """, nativeQuery = true)
    List<MostPopularJobProjection> findMostPopularJobs(@Param("startDate") ZonedDateTime startDate);
}
