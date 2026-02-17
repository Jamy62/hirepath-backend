package com.hirepath.hirepath_backend.repository.preferredlanguage;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import com.hirepath.hirepath_backend.model.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PreferredLanguageRepository extends CrudRepository<PreferredLanguage, Long> {
    Optional<PreferredLanguage> findByGuid(String guid);
    Optional<PreferredLanguage> findByName(String string);
}
