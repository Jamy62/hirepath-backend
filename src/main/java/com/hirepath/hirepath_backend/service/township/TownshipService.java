package com.hirepath.hirepath_backend.service.township;

import com.hirepath.hirepath_backend.model.request.township.TownshipCreateRequest;
import com.hirepath.hirepath_backend.model.request.township.TownshipUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface TownshipService {

    ResponseFormat townshipCreate(TownshipCreateRequest request, String adminEmail);

    ResponseFormat townshipList(String searchName, String orderBy, int first, int max);

    ResponseFormat townshipUpdate(String townshipGuid, TownshipUpdateRequest request, String email);

    ResponseFormat townshipDelete(String townshipGuid, String adminEmail);
}
