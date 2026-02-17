package com.hirepath.hirepath_backend.model.request.preferredindustry;

import com.hirepath.hirepath_backend.security.AutoNotBlank;
import lombok.Data;

@Data
@AutoNotBlank
public class PreferredIndustryCreateRequest {
    private String industryGuid;
}
