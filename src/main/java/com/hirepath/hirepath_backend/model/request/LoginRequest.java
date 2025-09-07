package com.hirepath.hirepath_backend.model.request;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AutoNotBlank
public class LoginRequest {
    private String email;
    private String password;
}
