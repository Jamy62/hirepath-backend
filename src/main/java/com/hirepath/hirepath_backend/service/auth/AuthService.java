package com.hirepath.hirepath_backend.service.auth;

import com.hirepath.hirepath_backend.model.response.LoginResponse;

public interface AuthService {
    LoginResponse login(String email, String password);
    void logout(String email);
    LoginResponse companyAccess(String companyGuid, String email);
    LoginResponse companySwitchBack(String email);
}
