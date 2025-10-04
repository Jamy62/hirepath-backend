package com.hirepath.hirepath_backend.service.province;

import com.hirepath.hirepath_backend.model.dto.province.ProvinceListDTO;
import com.hirepath.hirepath_backend.model.entity.province.Province;
import com.hirepath.hirepath_backend.model.request.province.ProvinceCreateRequest;
import com.hirepath.hirepath_backend.model.request.province.ProvinceUpdateRequest;

import java.util.List;

public interface ProvinceService {

    Province findByGuid(String guid);

    void provinceCreate(ProvinceCreateRequest request, String adminEmail);

    List<ProvinceListDTO> provinceList(String searchName, String orderBy, int first, int max);

    void provinceUpdate(String provinceGuid, ProvinceUpdateRequest request, String email);

    void provinceDelete(String provinceGuid, String adminEmail);
}
