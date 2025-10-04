package com.hirepath.hirepath_backend.service.user;

import com.hirepath.hirepath_backend.model.dto.user.UserDetailDTO;
import com.hirepath.hirepath_backend.model.dto.user.UserListDTO;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.model.request.user.RegisterRequest;
import com.hirepath.hirepath_backend.model.request.user.UserUpdateRequest;

import java.util.List;

public interface UserService {
    User findByGuid(String guid);
    String register(RegisterRequest request, String userType);
    List<UserListDTO> userList(String searchName, String orderBy, int first, int max);
    String userUpdate(String userGuid, UserUpdateRequest request, String email);
    String userDelete(String userGuid, String email);
    UserDetailDTO userDetail(String userGuid);
}
