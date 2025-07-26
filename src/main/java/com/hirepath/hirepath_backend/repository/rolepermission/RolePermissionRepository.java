package com.hirepath.hirepath_backend.repository.rolepermission;

import com.hirepath.hirepath_backend.model.entity.rolepermission.RolePermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends CrudRepository<RolePermission, Long> {
}
