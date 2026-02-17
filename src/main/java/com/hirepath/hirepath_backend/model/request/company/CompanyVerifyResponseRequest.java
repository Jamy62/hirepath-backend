package com.hirepath.hirepath_backend.model.request.company;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@AutoNotBlank
@Data
public class CompanyVerifyResponseRequest {
    private boolean response;
}
