package com.hirepath.hirepath_backend.model.dto.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class UserApplicationListDTO {
    private String applicationGuid;
    private String jobGuid;
    private String jobTitle;
    private String companyName;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime applicationDate;
}
