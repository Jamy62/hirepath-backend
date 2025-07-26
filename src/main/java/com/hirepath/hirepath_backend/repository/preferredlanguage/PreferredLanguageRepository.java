package com.hirepath.hirepath_backend.repository.preferredlanguage;

import com.hirepath.hirepath_backend.model.entity.preferredlanguage.PreferredLanguage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredLanguageRepository extends CrudRepository<PreferredLanguage, Long> {
}
