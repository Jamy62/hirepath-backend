package com.hirepath.hirepath_backend.service.preferredindustry;

import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;
import com.hirepath.hirepath_backend.model.request.preferredindustry.PreferredIndustryCreateRequest;

public interface PreferredIndustryService {
    PreferredIndustry findByGuid(String guid);
    void preferredIndustryCreate(PreferredIndustryCreateRequest request, String email);
    void preferredIndustryDelete(String preferredIndustryGuid, String email);
}
