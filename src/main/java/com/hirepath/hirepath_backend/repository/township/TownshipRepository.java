package com.hirepath.hirepath_backend.repository.township;

import com.hirepath.hirepath_backend.model.entity.township.Township;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownshipRepository extends CrudRepository<Township, Long> {
}
