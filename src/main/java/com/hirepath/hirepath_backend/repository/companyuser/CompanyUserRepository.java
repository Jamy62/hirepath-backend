package com.hirepath.hirepath_backend.repository.companyuser;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyUserRepository extends CrudRepository<CompanyUser, Long> {
    Optional<CompanyUser> findByUserAndCompanyAndIsDeleted(User user, Company company, boolean isDeleted);
    Optional<List<CompanyUser>> findByUserAndIsDeleted(User user, boolean isDeleted);
}
