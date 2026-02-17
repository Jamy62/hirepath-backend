package com.hirepath.hirepath_backend.model.dto.resume;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ResumeListDTO {
    private String name;
    private String filePath;
    private String guid;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
