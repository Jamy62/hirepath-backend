package com.hirepath.hirepath_backend.repository.application;

import com.hirepath.hirepath_backend.model.entity.application.Application;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
}
