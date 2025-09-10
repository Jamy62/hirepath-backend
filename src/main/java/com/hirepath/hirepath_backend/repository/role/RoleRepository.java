package com.hirepath.hirepath_backend.repository.role;

import com.hirepath.hirepath_backend.model.entity.role.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByGuid(String guid);
}
