package com.hirepath.hirepath_backend.repository.permission;

import com.hirepath.hirepath_backend.model.entity.permission.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
