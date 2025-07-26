package com.hirepath.hirepath_backend.repository.userlanguage;

import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLanguageRepository extends CrudRepository<UserLanguage, Long> {
}
