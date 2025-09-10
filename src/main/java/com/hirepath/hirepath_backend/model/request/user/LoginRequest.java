package com.hirepath.hirepath_backend.model.request.user;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AutoNotBlank
public class LoginRequest {
    private String email;
    private String password;
}
