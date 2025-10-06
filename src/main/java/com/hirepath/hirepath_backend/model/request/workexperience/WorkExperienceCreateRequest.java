package com.hirepath.hirepath_backend.model.request.workexperience;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AutoNotBlank
public class WorkExperienceCreateRequest {
    private String companyName;
    private String position;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String description;
}
