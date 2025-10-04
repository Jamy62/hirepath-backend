package com.hirepath.hirepath_backend.service.township;

import com.hirepath.hirepath_backend.model.dto.township.TownshipListDTO;
import com.hirepath.hirepath_backend.model.request.township.TownshipCreateRequest;
import com.hirepath.hirepath_backend.model.request.township.TownshipUpdateRequest;

import java.util.List;

public interface TownshipService {

    void townshipCreate(TownshipCreateRequest request, String adminEmail);

    List<TownshipListDTO> townshipList(String searchName, String orderBy, int first, int max);

    void townshipUpdate(String townshipGuid, TownshipUpdateRequest request, String email);

    void townshipDelete(String townshipGuid, String adminEmail);
}
