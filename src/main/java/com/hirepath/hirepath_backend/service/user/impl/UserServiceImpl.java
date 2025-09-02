package com.hirepath.hirepath_backend.service.user.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseFormat userList(int first, int max) {

    }

    public ResponseFormat adminRegister(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email already has an account");
            }

            Role role = roleRepository.findByName(VariableConstant.ADMIN)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin role not found"));
            User user = User.builder()
                    .role(role)
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .isActive(true)
                    .isDeleted(false)
                    .guid(UUID.randomUUID().toString())
                    .createdAt(ZonedDateTime.now())
                    .build();

            userRepository.save(user);

            return ResponseFormat.createSuccessResponse(user.getName(), "Admin registered successfully");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public ResponseFormat userRegister(RegisterRequest request) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email already has an account");
            }

            Role role = roleRepository.findByName(VariableConstant.USER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User role not found"));
            User user = User.builder()
                    .role(role)
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .isActive(true)
                    .isDeleted(false)
                    .guid(UUID.randomUUID().toString())
                    .createdAt(ZonedDateTime.now())
                    .build();

            userRepository.save(user);

            return ResponseFormat.createSuccessResponse(user.getName(), "Admin registered successfully");
        }
        catch (Exception e) {
            throw e;
        }
    }
}
