package com.hirepath.hirepath_backend.service.role.impl;

import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByGuid(String guid) {
        try {
            return roleRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
        } catch (Exception e) {
            throw e;
        }
    }
}
