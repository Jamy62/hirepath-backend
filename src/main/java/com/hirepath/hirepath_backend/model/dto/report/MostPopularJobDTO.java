package com.hirepath.hirepath_backend.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularJobDTO {
    private String jobTitle;
    private Long applicationCount;
    private String companyName;
    private String logo;
    private String website;
}
