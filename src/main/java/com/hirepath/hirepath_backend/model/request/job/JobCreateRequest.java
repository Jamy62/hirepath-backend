package com.hirepath.hirepath_backend.model.request.job;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class JobCreateRequest {

    private String townshipGuid;
    private String experienceLevelGuid;
    private String jobTypeGuid;
    private String jobFunctionGuid;
    private List<String> industryGuids;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private Double minSalary;
    private Double maxSalary;
}
