package com.hirepath.hirepath_backend.controller.user;

import com.hirepath.hirepath_backend.model.dto.company.CompanyListDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserProfileDTO;
import com.hirepath.hirepath_backend.model.request.user.RegisterRequest;
import com.hirepath.hirepath_backend.model.request.user.UserUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> adminRegister(@Valid @RequestBody RegisterRequest request) {
        String response = userService.register(request, "admin");
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Admin registered successfully"));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseFormat> userRegister(@Valid @RequestBody RegisterRequest request) {
        String response = userService.register(request, "user");
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "User registered successfully"));
    }

    @GetMapping("/user-list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> userList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<UserListDTO> response = userService.userList(searchName, orderBy, "USER" , first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "User list retrieved successfully"));
    }

    @GetMapping("/admin-list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> adminList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        List<UserListDTO> response = userService.userList(searchName, orderBy, "ADMIN", first, max);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Admin list retrieved successfully"));
    }

    @PutMapping("/update/admin/{userGuid}")
    @PreAuthorize("hasAnyRole('SYSTEM')")
    public ResponseEntity<ResponseFormat> userUpdate(@PathVariable String userGuid,
                                                     @Valid @RequestBody UserUpdateRequest request, Principal principal) {
        String response = userService.userUpdate(userGuid, request, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "User updated successfully"));
    }

    @DeleteMapping("/delete/admin/{userGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> userDelete(
            @PathVariable(value = "userGuid") String userGuid,
            Principal principal) {
        String response = userService.userDelete(userGuid, principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "User deleted successfully"));
    }

    @GetMapping("/detail/{userGuid}")
    public ResponseEntity<ResponseFormat> userDetail(
            @PathVariable(value = "userGuid") String userGuid) {
        UserDetailDTO response = userService.userDetail(userGuid);
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Successfully retrieved user detail"));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('SYSTEM')")
    public ResponseEntity<ResponseFormat> userProfile(
            Principal principal) {
        UserProfileDTO response = userService.userProfile(principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Successfully retrieved user profile"));
    }

    @GetMapping("/companies")
    @PreAuthorize("hasAnyRole('SYSTEM')")
    public ResponseEntity<ResponseFormat> getUserCompanies(Principal principal) {
        List<CompanyListDTO> response = userService.getUserCompanies(principal.getName());
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(response, "Successfully retrieved user companies"));
    }
}
