package com.hirepath.hirepath_backend.repository.application;

import com.hirepath.hirepath_backend.model.dto.application.CompanyApplicationListProjection;
import com.hirepath.hirepath_backend.model.dto.application.UserApplicationListProjection;
import com.hirepath.hirepath_backend.model.entity.application.Application;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.job.Job;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    Optional<Application> findByGuid(String guid);
    Optional<Application> findByUserAndJobAndStatusAndIsDeletedFalse(User user, Job job, String status);

    @Query("SELECT a FROM Application a WHERE a.user = :user AND a.job.company = :company AND a.status = :status AND a.isDeleted = false")
    List<Application> findAllByUserAndCompanyAndStatusAndIsDeletedFalse(@Param("user") User user, @Param("company") Company company, @Param("status") String status);

    @Query(value = """
            SELECT
                a.guid AS applicationGuid,
                j.title AS jobTitle,
                c.name AS companyName,
                a.status AS status,
                a.application_date AS applicationDate
            FROM applications a
            JOIN jobs j ON a.job_id = j.id
            JOIN companies c ON j.company_id = c.id
            WHERE a.user_id = :userId
            ORDER BY a.application_date DESC
            """, nativeQuery = true)
    List<UserApplicationListProjection> findAllByUser(@Param("userId") Long userId);

    @Query(value = """
            SELECT
                a.guid AS applicationGuid,
                j.title AS jobTitle,
                u.name AS applicantName,
                r.guid AS resumeGuid,
                a.status AS status,
                a.application_date AS applicationDate
            FROM applications a
            JOIN jobs j ON a.job_id = j.id
            JOIN users u ON a.user_id = u.id
            JOIN resumes r ON a.resume_id = r.id
            WHERE j.company_id = :companyId
            ORDER BY a.application_date DESC
            """, nativeQuery = true)
    List<CompanyApplicationListProjection> findAllByCompany(@Param("companyId") Long companyId);
}
