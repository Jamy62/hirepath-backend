package com.hirepath.hirepath_backend.repository.companyuser;

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
}
