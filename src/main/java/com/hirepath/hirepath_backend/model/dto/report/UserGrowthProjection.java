package com.hirepath.hirepath_backend.model.dto.report;

import java.time.LocalDate;

public interface UserGrowthProjection {
    LocalDate getDate();
    Long getUserCount();
}
