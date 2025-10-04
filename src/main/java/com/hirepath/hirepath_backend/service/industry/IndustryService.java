package com.hirepath.hirepath_backend.service.industry;

import com.hirepath.hirepath_backend.model.dto.industry.IndustryListDTO;
import com.hirepath.hirepath_backend.model.request.industry.IndustryCreateRequest;
import com.hirepath.hirepath_backend.model.request.industry.IndustryUpdateRequest;

import java.util.List;

public interface IndustryService {

    void industryCreate(IndustryCreateRequest request, String adminEmail);

    List<IndustryListDTO> industryList(String searchName, String orderBy, int first, int max);

    void industryUpdate(String industryGuid, IndustryUpdateRequest request, String email);

    void industryDelete(String industryGuid, String adminEmail);
}
