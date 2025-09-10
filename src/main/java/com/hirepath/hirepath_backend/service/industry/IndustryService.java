package com.hirepath.hirepath_backend.service.industry;

import com.hirepath.hirepath_backend.model.request.industry.IndustryCreateRequest;
import com.hirepath.hirepath_backend.model.request.industry.IndustryUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface IndustryService {

    ResponseFormat industryCreate(IndustryCreateRequest request, String adminEmail);

    ResponseFormat industryList(String searchName, String orderBy, int first, int max);

    ResponseFormat industryUpdate(String industryGuid, IndustryUpdateRequest request, String email);

    ResponseFormat industryDelete(String industryGuid, String adminEmail);
}
