package com.hirepath.hirepath_backend.model.dto.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
public class ApplicationDetailDTO {

    private String applicationGuid;
    private String applicationStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime applicationDate;

    private String coverLetter;
    private String resumeFilePath;
}
