package com.hirepath.hirepath_backend.service.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileStorageService {
    String storeProfile(MultipartFile file, String personGuid);
    String storeResume(MultipartFile file, String resumeGuid);
}
