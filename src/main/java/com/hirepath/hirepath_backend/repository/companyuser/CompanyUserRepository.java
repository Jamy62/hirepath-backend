package com.hirepath.hirepath_backend.repository.companyuser;

import com.hirepath.hirepath_backend.model.dto.report.CompanyEmployeeGrowthProjection;
import com.hirepath.hirepath_backend.model.dto.report.EmployeesPerPositionProjection;
import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyUserRepository extends CrudRepository<CompanyUser, Long> {
    Optional<CompanyUser> findByGuid(String guid);
    List<CompanyUser> findAllByCompanyAndIsDeletedFalse(Company company);
    Optional<CompanyUser> findByUserAndCompanyAndIsDeletedFalse(User user, Company company);

    @Query(value = "SELECT * FROM company_user WHERE user_id = :userId AND company_id = :companyId", nativeQuery = true)
    Optional<CompanyUser> findByUserAndCompanyIncludingDeleted(@Param("userId") Long userId, @Param("companyId") Long companyId);

    Optional<CompanyUser> findByUserAndCompanyAndIsDeletedTrue(User user, Company company);
    List<CompanyUser> findAllByUserAndIsDeletedFalse(User user);

    @Query(value = """
            SELECT
                CAST(created_at AS DATE) AS date,
                COUNT(id) AS employeeCount
            FROM company_user
            WHERE company_id = :companyId
            AND is_deleted = 0
            GROUP BY CAST(created_at AS DATE)
            ORDER BY date
            """, nativeQuery = true)
    List<CompanyEmployeeGrowthProjection> findCompanyEmployeeGrowth(@Param("companyId") Long companyId);

    @Query(value = """
            SELECT
                p.name AS positionName,
                COUNT(cup.id) AS employeeCount
            FROM company_user_position cup
            JOIN positions p ON cup.position_id = p.id
            JOIN company_user cu ON cup.company_user_id = cu.id
            WHERE cu.company_id = :companyId
            AND cup.is_deleted = 0
            AND p.is_deleted = 0
            AND cu.is_deleted = 0
            GROUP BY p.name
            """, nativeQuery = true)
    List<EmployeesPerPositionProjection> findEmployeesPerPosition(@Param("companyId") Long companyId);
}
