package com.hirepath.hirepath_backend.service.preferredindustry;

import com.hirepath.hirepath_backend.model.entity.preferredindustry.PreferredIndustry;

public interface PreferredIndustryService {
    PreferredIndustry findByGuid(String guid);
}
