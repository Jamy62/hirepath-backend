package com.hirepath.hirepath_backend.service.companyuserposition;

import com.hirepath.hirepath_backend.model.dto.companyuserposition.CompanyUserPositionListDTO;
import com.hirepath.hirepath_backend.model.entity.companyuserposition.CompanyUserPosition;
import com.hirepath.hirepath_backend.model.request.companyuserposition.CompanyUserPositionListRequest;
import com.hirepath.hirepath_backend.model.request.companyuserposition.PositionListRequest;

import java.util.List;

public interface CompanyUserPositionService {
    CompanyUserPosition findByGuid(String guid);
    void companyUserPositionsAssign(String companyUserGuid, PositionListRequest request, String email, String companyGuid);
    void companyUserPositionsDelete(CompanyUserPositionListRequest request, String email, String companyGuid);
    List<CompanyUserPositionListDTO> companyUserPositionList(String companyUserGuid, String companyGuid);
}
