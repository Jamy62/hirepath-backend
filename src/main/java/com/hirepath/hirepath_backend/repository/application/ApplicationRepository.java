package com.hirepath.hirepath_backend.repository.application;

import com.hirepath.hirepath_backend.model.entity.application.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    Optional<Application> findByGuid(String guid);
}
