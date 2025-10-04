package com.hirepath.hirepath_backend.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String guid;
}
