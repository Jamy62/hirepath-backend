package com.hirepath.hirepath_backend.model.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class JobListDTO {

    private String guid;
    private String title;
    private String minSalary;
    private String maxSalary;
    private String location;

    private String companyName;
    private String companyLogo;

    private String jobType;
    private String experienceLevel;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime createdAt;

    private Boolean isApplied;
    private Boolean isEmployed;
}
