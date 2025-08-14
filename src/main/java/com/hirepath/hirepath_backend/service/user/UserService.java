package com.hirepath.hirepath_backend.service.user;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface UserService {
    ResponseFormat userList(int first, int max);
}
