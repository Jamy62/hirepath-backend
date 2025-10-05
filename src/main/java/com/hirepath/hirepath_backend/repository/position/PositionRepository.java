package com.hirepath.hirepath_backend.repository.position;

import com.hirepath.hirepath_backend.model.entity.position.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {
    Optional<Position> findByGuid(String guid);
}
