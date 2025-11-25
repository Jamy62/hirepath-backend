package com.hirepath.hirepath_backend.model.request.company;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import jakarta.validation.constraints.Email;
import lombok.*;

@AutoNotBlank
@Data
public class CompanyRegisterRequest {
    private String name;
    private String description;
    private String phone;
    @Email(message = "Email must be a valid email address")
    private String email;
}
