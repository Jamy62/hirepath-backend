package com.hirepath.hirepath_backend.service.admin;

import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface AdminService {
    ResponseFormat adminRegister(RegisterRequest request);
}
