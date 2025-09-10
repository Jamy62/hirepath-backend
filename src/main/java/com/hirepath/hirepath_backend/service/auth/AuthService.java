package com.hirepath.hirepath_backend.service.auth;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface AuthService {
    ResponseFormat login(String email, String password);
    void logout(String email);
    ResponseFormat companyAccess(String companyGuid, String email);
}
