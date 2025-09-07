package com.hirepath.hirepath_backend.service.province;

import com.hirepath.hirepath_backend.model.request.ProvinceCreateRequest;
import com.hirepath.hirepath_backend.model.request.ProvinceUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface ProvinceService {

    ResponseFormat provinceCreate(ProvinceCreateRequest request, String adminEmail);

    ResponseFormat provinceList(String searchName, String orderBy, int first, int max);

    ResponseFormat provinceUpdate(String provinceGuid, ProvinceUpdateRequest request, String email);

    ResponseFormat provinceDelete(String provinceGuid, String adminEmail);
}
