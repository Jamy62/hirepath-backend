package com.hirepath.hirepath_backend.model.request.education;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AutoNotBlank
public class EducationCreateRequest {
    private String institution;
    private String degree;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
