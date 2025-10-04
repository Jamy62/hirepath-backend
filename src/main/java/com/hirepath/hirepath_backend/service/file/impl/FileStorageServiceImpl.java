package com.hirepath.hirepath_backend.service.file.impl;

import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.file.FileStorageService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final Path imageStorageLocation = Paths.get("files/images").toAbsolutePath().normalize();
    private final Path resumeStorageLocation = Paths.get("files/resumes").toAbsolutePath().normalize();
    private final UserService userService;
    private final UserRepository userRepository;

    public String storeProfile(MultipartFile file, String userGuid) {
        User user = userService.findByGuid(userGuid);
        String storeName = UUID.randomUUID().toString();
        user.setProfile(storeName);
        user.setUpdatedAt(ZonedDateTime.now());
        user.setUpdatedBy(user.getId());
        userRepository.save(user);

        return store(file, imageStorageLocation, storeName);
    }

    public String storeResume(MultipartFile file, String resumeGuid) {
        // todo: find resume by resumeGuid and save the file name to the resume's path field
        String storeName = UUID.randomUUID().toString();
        return store(file, resumeStorageLocation, storeName);
    }

    private String store(MultipartFile file, Path location, String storeName) {
        try {
            Files.createDirectories(imageStorageLocation);
            Files.createDirectories(resumeStorageLocation);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create the directory where the uploaded files will be stored.", e);
        }

        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = "";
        try {
            if (originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            if (fileExtension.isBlank() || !isValidExtension(fileExtension, location)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type.");
            }

            String fileName = storeName + fileExtension;

            Path targetLocation = location.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not store file " + originalFileName + ". Please try again", e);
        }
    }

    private boolean isValidExtension(String fileExtension, Path location) {
        if (location.equals(imageStorageLocation)) {
            return fileExtension.equalsIgnoreCase(".jpg") ||
                    fileExtension.equalsIgnoreCase(".jpeg") ||
                    fileExtension.equalsIgnoreCase(".png") ||
                    fileExtension.equalsIgnoreCase(".gif");
        } else if (location.equals(resumeStorageLocation)) {
            return fileExtension.equalsIgnoreCase(".pdf") ||
                    fileExtension.equalsIgnoreCase(".doc") ||
                    fileExtension.equalsIgnoreCase(".docx");
        }
        return false;
    }
}
