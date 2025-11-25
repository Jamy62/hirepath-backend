package com.hirepath.hirepath_backend.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
    String storeProfile(MultipartFile file, String personGuid, String email);
    String storeLogo(MultipartFile file, String companyGuid, String email);
    String storeBanner(MultipartFile file, String companyGuid, String email);
    String storeLocation(MultipartFile file, String companyGuid, String name, String address, String email);
    String storeResume(MultipartFile file, String userGuid, String name, String email);
    void deleteResume(String resumeGuid, String name);
}
