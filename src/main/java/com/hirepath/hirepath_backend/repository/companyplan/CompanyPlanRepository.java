package com.hirepath.hirepath_backend.repository.companyplan;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyplan.CompanyPlan;
import com.hirepath.hirepath_backend.model.entity.plan.Plan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyPlanRepository extends CrudRepository<CompanyPlan, Long> {
    Optional<CompanyPlan> findByGuid(String guid);
    Optional<CompanyPlan> findByCompanyAndIsDeletedFalse(Company company);
    Optional<CompanyPlan> findByCompanyAndIsDeletedTrue(Company company);

    @Query("SELECT cp FROM CompanyPlan cp JOIN cp.plan p WHERE cp.company = :company AND cp.isActive = true AND cp.isDeleted = false AND p.isDeleted = false")
    Optional<CompanyPlan> findByCompanyAndIsActiveTrue(Company company);

    List<CompanyPlan> findAllByPlanAndIsDeletedFalse(Plan plan);
}
