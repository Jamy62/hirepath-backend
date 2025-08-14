package com.hirepath.hirepath_backend.service.auth;

import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

import java.security.Principal;

public interface AuthService {
    ResponseFormat register(String email, String password, String name);
    ResponseFormat login(String email, String password);
    void logout(String email);
    ResponseFormat companyAccess(String companyGuid, String email);
}
