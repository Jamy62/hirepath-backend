package com.hirepath.hirepath_backend.repository.location;

import com.hirepath.hirepath_backend.model.entity.location.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findByGuid(String guid);
}
