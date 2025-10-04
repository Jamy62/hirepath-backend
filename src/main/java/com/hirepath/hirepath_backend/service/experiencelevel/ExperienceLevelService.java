package com.hirepath.hirepath_backend.service.experiencelevel;

import com.hirepath.hirepath_backend.model.dto.experiencelevel.ExperienceLevelListDTO;
import com.hirepath.hirepath_backend.model.entity.experiencelevel.ExperienceLevel;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelCreateRequest;
import com.hirepath.hirepath_backend.model.request.experiencelevel.ExperienceLevelUpdateRequest;

import java.util.List;

public interface ExperienceLevelService {

    ExperienceLevel findByGuid(String guid);

    void experienceLevelCreate(ExperienceLevelCreateRequest request, String adminEmail);

    List<ExperienceLevelListDTO> experienceLevelList(String searchName, String orderBy, int first, int max);

    void experienceLevelUpdate(String experienceLevelGuid, ExperienceLevelUpdateRequest request, String email);

    void experienceLevelDelete(String experienceLevelGuid, String adminEmail);
}
