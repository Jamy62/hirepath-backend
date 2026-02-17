package com.hirepath.hirepath_backend.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeProfile(MultipartFile file, String userGuid, String email);
    String storeLogo(MultipartFile file, String companyGuid, String email);
    String storeBanner(MultipartFile file, String companyGuid, String email);
    String storeLocation(MultipartFile file, String companyGuid, String name, String address, String email);
    String storeResume(MultipartFile file, String userGuid, String name, String email);
    void deleteResume(String resumeGuid, String email);
    void deleteLocation(String locationGuid, String email);
    String getResumeFilenameByGuid(String resumeGuid);
}
