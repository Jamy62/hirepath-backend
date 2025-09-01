package com.hirepath.hirepath_backend.service.user;

import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface UserService {
    ResponseFormat adminRegister(RegisterRequest request);
    ResponseFormat userRegister(RegisterRequest request);
    ResponseFormat userList(int first, int max);
}
