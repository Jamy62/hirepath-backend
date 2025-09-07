package com.hirepath.hirepath_backend.service.experiencelevel;

import com.hirepath.hirepath_backend.model.request.ExperienceLevelCreateRequest;
import com.hirepath.hirepath_backend.model.request.ExperienceLevelUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface ExperienceLevelService {

    ResponseFormat experienceLevelCreate(ExperienceLevelCreateRequest request, String adminEmail);

    ResponseFormat experienceLevelList(String searchName, String orderBy, int first, int max);

    ResponseFormat experienceLevelUpdate(String experienceLevelGuid, ExperienceLevelUpdateRequest request, String email);

    ResponseFormat experienceLevelDelete(String experienceLevelGuid, String adminEmail);
}
