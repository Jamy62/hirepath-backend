package com.hirepath.hirepath_backend.repository.userlanguage;

import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.entity.userlanguage.UserLanguage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLanguageRepository extends CrudRepository<UserLanguage, Long> {
    Optional<UserLanguage> findByGuid(String guid);
    List<UserLanguage> findAllByUserAndIsDeletedFalse(User user);
}
