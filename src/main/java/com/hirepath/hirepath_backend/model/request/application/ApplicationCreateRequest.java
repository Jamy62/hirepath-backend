package com.hirepath.hirepath_backend.model.request.application;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class ApplicationCreateRequest {
    private String jobGuid;
    private String resumeGuid;
    private String coverLetter;
}
