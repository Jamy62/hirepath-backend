package com.hirepath.hirepath_backend.service.user;

import com.hirepath.hirepath_backend.model.request.RegisterRequest;
import com.hirepath.hirepath_backend.model.request.UserUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface UserService {
    ResponseFormat register(RegisterRequest request, String userType);
    ResponseFormat userList(String searchName, String orderBy, int first, int max);
    ResponseFormat userUpdate(String userGuid, UserUpdateRequest request, String email);
    ResponseFormat userDelete(String userGuid, String email);
}
