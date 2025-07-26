package com.hirepath.hirepath_backend.repository.company;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {
    Optional<Company> findByGuid(String guid);
}
