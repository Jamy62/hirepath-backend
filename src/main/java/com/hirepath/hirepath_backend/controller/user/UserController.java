package com.hirepath.hirepath_backend.controller.user;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> adminRegister(@Valid @RequestBody RegisterRequest request) {
        ResponseFormat responseFormat = userService.register(request, "admin");
        return ResponseEntity.ok(responseFormat);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseFormat> userRegister(@Valid @RequestBody RegisterRequest request) {
        ResponseFormat responseFormat = userService.register(request, "user");
        return ResponseEntity.ok(responseFormat);
    }

    @GetMapping("/list/admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> userList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DESC") String orderBy,
            @RequestParam(value = "first", required = false, defaultValue = "0") int first,
            @RequestParam(value = "max", required = false, defaultValue = "" + Integer.MAX_VALUE) int max) {
        ResponseFormat responseFormat = userService.userList(searchName, orderBy, first, max);
        return ResponseEntity.ok(responseFormat);
    }

    @PutMapping("/update/admin/{userGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> userUpdate(@PathVariable String userGuid,
                                                     @Valid @RequestBody UserUpdateRequest request, Principal principal) {
        ResponseFormat responseFormat = userService.userUpdate(userGuid, request, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }

    @DeleteMapping("/delete/admin/{userGuid}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ResponseFormat> userDelete(
            @PathVariable(value = "userGuid") String userGuid,
            Principal principal) {
        ResponseFormat responseFormat = userService.userDelete(userGuid, principal.getName());
        return ResponseEntity.ok(responseFormat);
    }
}
