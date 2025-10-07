package com.hirepath.hirepath_backend.repository.preferredindustry;

import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferredIndustryRepository extends CrudRepository<PreferredIndustry, Long> {
    Optional<PreferredIndustry> findByGuid(String guid);
    List<PreferredIndustry> findAllByUserAndIsDeletedFalse(User user);
}
