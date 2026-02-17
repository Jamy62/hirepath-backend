package com.hirepath.hirepath_backend.model.dto.companyplan;

import lombok.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ActivePlanDTO {
    private String plan;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean isActive;
}
