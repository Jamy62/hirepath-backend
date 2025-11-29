package com.hirepath.hirepath_backend.model.dto.report;

public interface MostPopularJobProjection {
    String getJobTitle();
    Long getApplicationCount();
    String getCompanyName();
    String getLogo();
    String getWebsite();
}
