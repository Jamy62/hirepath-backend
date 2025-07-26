package com.hirepath.hirepath_backend.repository.language;

import com.hirepath.hirepath_backend.model.entity.language.Language;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {
}
