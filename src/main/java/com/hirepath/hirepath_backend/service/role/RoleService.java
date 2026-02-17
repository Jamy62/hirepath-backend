package com.hirepath.hirepath_backend.service.role;

import com.hirepath.hirepath_backend.model.entity.role.Role;

public interface RoleService {
    Role findByGuid(String guid);
}
