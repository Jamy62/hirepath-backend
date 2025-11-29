package com.hirepath.hirepath_backend.model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeesPerPositionDTO {
    private String positionName;
    private Long employeeCount;
}
