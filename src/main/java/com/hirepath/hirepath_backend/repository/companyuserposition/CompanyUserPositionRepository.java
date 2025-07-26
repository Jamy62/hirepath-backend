package com.hirepath.hirepath_backend.repository.companyuserposition;

import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyUserPositionRepository extends CrudRepository<CompanyUserPosition, Long> {
}
