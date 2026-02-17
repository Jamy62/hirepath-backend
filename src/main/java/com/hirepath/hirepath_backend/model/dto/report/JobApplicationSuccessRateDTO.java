package com.hirepath.hirepath_backend.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationSuccessRateDTO {
    private String companyName;
    private Double successRate;
    private String logo;
}
