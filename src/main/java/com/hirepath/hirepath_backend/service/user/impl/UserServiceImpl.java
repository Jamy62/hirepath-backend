package com.hirepath.hirepath_backend.service.user.impl;

import com.hirepath.hirepath_backend.constant.VariableConstant;
import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListProjection;
import com.hirepath.hirepath_backend.model.dto.user.UserProfileDTO;
import com.hirepath.hirepath_backend.model.entity.role.Role;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.user.RegisterRequest;
import com.hirepath.hirepath_backend.model.request.user.UserUpdateRequest;
import com.hirepath.hirepath_backend.repository.role.RoleRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.role.RoleService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User findByGuid(String guid) {
        try {
            return userRepository.findByGuid(guid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } catch (Exception e) {
            throw e;
        }
    }

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        } catch (Exception e) {
            throw e;
        }
    }

//    public void isOwner(User user, String email) {
//        try {
//            if (!user.equals(findByEmail(email))) {
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this account");
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    public String register(RegisterRequest request, String userType) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email already has an account");
            }

            Role role = roleRepository.findByName(userType.equals("admin") ? VariableConstant.ADMIN : VariableConstant.USER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));
            User user = User.builder()
                    .role(role)
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .isActive(true)
                    .isDeleted(false)
                    .isBlocked(false)
                    .guid(UUID.randomUUID().toString())
                    .createdAt(ZonedDateTime.now())
                    .build();

            userRepository.save(user);

            return user.getName();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public List<UserListDTO> userList(String searchName, String orderBy, String role, int first, int max) {
        try {
            if (searchName.isBlank()) {
                searchName = null;
            }

            if (orderBy.equals(VariableConstant.DESC) || orderBy.equals(VariableConstant.ASC)) {
                List<UserListProjection> userListProjection = userRepository.findAllUsersAdminPanal(searchName, orderBy, role, first, max);

                return userListProjection.stream()
                        .map(p -> UserListDTO.builder()
                                .name(p.getName())
                                .fullName(p.getFullName())
                                .email(p.getEmail())
                                .mobile(p.getMobile())
                                .profile(p.getProfile())
                                .roleName(p.getRoleName())
                                .isActive(p.getIsActive())
                                .isBlocked(p.getIsBlocked())
                                .guid(p.getGuid())
                                .createdAt(p.getCreatedAt() != null ? p.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .updatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .lastLoginAt(p.getLastLoginAt() != null ? p.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                                .build())
                        .toList();
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "please enter either ASC or DESC for orderBy");
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String userUpdate(String userGuid, UserUpdateRequest request, String email) {
        try {
            log.info("User update initiate");
            User user = findByGuid(userGuid);
            User updatedBy = findByEmail(email);

            if (request.getName() != null && !request.getName().isBlank()) {
                user.setName(request.getName());
            }
            if (request.getFullName() != null && !request.getFullName().isBlank()) {
                user.setFullName(request.getFullName());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()) {
                user.setEmail(request.getEmail());
            }
            if (request.getMobile() != null && !request.getMobile().isBlank()) {
                user.setMobile(request.getMobile());
            }
            if (request.getRoleGuid() != null && !request.getRoleGuid().isBlank()) {
                Role role = roleService.findByGuid(request.getRoleGuid());
                user.setRole(role);
            }
            if (request.getIsActive() instanceof Boolean) {
                user.setIsActive(request.getIsActive());
            }
            if (request.getIsBlocked() instanceof Boolean) {
                user.setIsBlocked(request.getIsBlocked());
            }

            user.setUpdatedAt(ZonedDateTime.now());
            user.setUpdatedBy(updatedBy.getId());
            log.info(String.valueOf(user.getCreatedAt()));
            userRepository.save(user);

            return user.getGuid();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String userDelete(String guid, String email) {
        try {
            User user = findByGuid(guid);
            User deletedBy = findByEmail(email);

            user.setIsDeleted(true);
            user.setUpdatedAt(ZonedDateTime.now());
            user.setUpdatedBy(deletedBy.getId());
            userRepository.save(user);

            return guid;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserDetailDTO userDetail(String userGuid) {
        try {
            User user = findByGuid(userGuid);

            return UserDetailDTO.builder()
                    .name(user.getName())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .profile(user.getProfile())
                    .role(user.getRole())
                    .isActive(user.getIsActive())
                    .isBlocked(user.getIsBlocked())
                    .guid(user.getGuid())
                    .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .lastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .build();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserProfileDTO userProfile(String email) {
        try {
            User user = findByEmail(email);

            return UserProfileDTO.builder()
                    .name(user.getName())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .mobile(user.getMobile())
                    .profile(user.getProfile())
                    .role(user.getRole())
                    .isActive(user.getIsActive())
                    .isBlocked(user.getIsBlocked())
                    .guid(user.getGuid())
                    .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .lastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().toInstant().atZone(ZoneId.systemDefault()) : null)
                    .build();
        }
        catch (Exception e) {
            throw e;
        }
    }
}
