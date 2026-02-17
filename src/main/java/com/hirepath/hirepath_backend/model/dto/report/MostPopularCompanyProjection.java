package com.hirepath.hirepath_backend.model.dto.report;

public interface MostPopularCompanyProjection {
    String getCompanyName();
    Long getApplicationCount();
    String getLogo();
    String getWebsite();
}
