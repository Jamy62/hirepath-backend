package com.hirepath.hirepath_backend.repository.job;

import com.hirepath.hirepath_backend.model.dto.job.JobListProjection;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByGuid(String guid);

    @Query(value = """
            SELECT
                j.guid AS guid,
                j.title AS title,
                j.salary AS salary,
                t.name AS location,
                c.name AS companyName,
                c.logo AS companyLogo,
                (c.verification_status = 'TRUE') AS isCompanyVerified,
                jt.name AS jobType,
                el.name AS experienceLevel,
                j.created_at AS createdAt
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
                job_industries ji ON j.id = ji.job_id
            WHERE
                j.is_deleted = 0 AND j.is_active = 1
                AND (:searchTitle IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :searchTitle, '%')))
                AND (:companyGuid IS NULL OR c.guid = :companyGuid)
                AND (:provinceGuid IS NULL OR p.guid = :provinceGuid)
                AND (:townshipGuid IS NULL OR t.guid = :townshipGuid)
                AND (:jobTypeGuid IS NULL OR jt.guid = :jobTypeGuid)
                AND (:experienceLevelGuid IS NULL OR el.guid = :experienceLevelGuid)
                AND (:industryGuid IS NULL OR ji.industry_id = (SELECT id FROM industries WHERE guid = :industryGuid))
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
            @Param("salary") Double salary,
            @Param("orderBy") String orderBy,
            @Param("first") int first,
            @Param("max") int max);
}
