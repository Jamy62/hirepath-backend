package com.hirepath.hirepath_backend.model.request.user;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@AutoNotBlank
@Getter
@Setter
public class RegisterRequest {
    @Email(message = "Email must be a valid email address")
    private String email;
    private String password;
    private String name;
    private String fullName;
}
