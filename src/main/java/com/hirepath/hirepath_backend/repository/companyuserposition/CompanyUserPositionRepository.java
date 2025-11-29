package com.hirepath.hirepath_backend.repository.companyuserposition;

import com.hirepath.hirepath_backend.model.entity.companyuser.CompanyUser;
import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.model.entity.position.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyUserPositionRepository extends CrudRepository<CompanyUserPosition, Long> {
    Optional<CompanyUserPosition> findByGuid(String guid);
    List<CompanyUserPosition> findAllByCompanyUserAndIsDeletedFalse(CompanyUser companyUser);
    List<CompanyUserPosition> findAllByPositionAndIsDeletedFalse(Position position);
}
