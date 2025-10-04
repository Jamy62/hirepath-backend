package com.hirepath.hirepath_backend.service.language;

import com.hirepath.hirepath_backend.model.dto.language.LanguageListDTO;
import com.hirepath.hirepath_backend.model.request.language.LanguageCreateRequest;
import com.hirepath.hirepath_backend.model.request.language.LanguageUpdateRequest;

import java.util.List;

public interface LanguageService {

    void languageCreate(LanguageCreateRequest request, String adminEmail);

    List<LanguageListDTO> languageList(String searchName, String orderBy, int first, int max);

    void languageUpdate(String languageGuid, LanguageUpdateRequest request, String email);

    void languageDelete(String languageGuid, String adminEmail);
}
