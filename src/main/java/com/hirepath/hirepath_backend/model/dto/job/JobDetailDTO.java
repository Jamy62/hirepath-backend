package com.hirepath.hirepath_backend.model.dto.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class JobDetailDTO {

    private String guid;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private Double minSalary;
    private Double maxSalary;

    private String jobType;
    private String experienceLevel;
    private String location;

    private List<String> industries;

    private String companyGuid;
    private String companyName;
    private String companyLogo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime postedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime expireDate;
}
