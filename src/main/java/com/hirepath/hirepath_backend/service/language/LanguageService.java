package com.hirepath.hirepath_backend.service.language;

import com.hirepath.hirepath_backend.model.request.LanguageCreateRequest;
import com.hirepath.hirepath_backend.model.request.LanguageUpdateRequest;
import com.hirepath.hirepath_backend.model.response.ResponseFormat;

public interface LanguageService {

    ResponseFormat languageCreate(LanguageCreateRequest request, String adminEmail);

    ResponseFormat languageList(String searchName, String orderBy, int first, int max);

    ResponseFormat languageUpdate(String languageGuid, LanguageUpdateRequest request, String email);

    ResponseFormat languageDelete(String languageGuid, String adminEmail);
}
