package com.hirepath.hirepath_backend.service.file.impl;

import com.hirepath.hirepath_backend.model.entity.company.Company;
import com.hirepath.hirepath_backend.model.entity.location.Location;
import com.hirepath.hirepath_backend.model.entity.resume.Resume;
import com.hirepath.hirepath_backend.model.entity.user.User;
import com.hirepath.hirepath_backend.repository.company.CompanyRepository;
import com.hirepath.hirepath_backend.repository.location.LocationRepository;
import com.hirepath.hirepath_backend.repository.resume.ResumeRepository;
import com.hirepath.hirepath_backend.repository.user.UserRepository;
import com.hirepath.hirepath_backend.service.company.CompanyService;
import com.hirepath.hirepath_backend.service.file.FileStorageService;
import com.hirepath.hirepath_backend.service.location.LocationService;
import com.hirepath.hirepath_backend.service.resume.ResumeService;
import com.hirepath.hirepath_backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;
import java.io.OutputStream;
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
    private final Path logoStorageLocation = Paths.get("files/logos").toAbsolutePath().normalize();
    private final Path bannerStorageLocation = Paths.get("files/banners").toAbsolutePath().normalize();
    private final Path locationStorageLocation = Paths.get("files/locations").toAbsolutePath().normalize();
    private final Path resumeStorageLocation = Paths.get("files/resumes").toAbsolutePath().normalize();

    private final UserService userService;
    private final UserRepository userRepository;
    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;

    public String storeProfile(MultipartFile file, String userGuid, String email) {
        User user = userService.findByGuid(userGuid);
        String storeName = UUID.randomUUID().toString();
        User uploader = userService.findByEmail(email);

        user.setProfile(storeName);
        user.setUpdatedAt(ZonedDateTime.now());
        user.setUpdatedBy(uploader.getId());
        userRepository.save(user);

        return store(file, imageStorageLocation, storeName);
    }

    public String storeLogo(MultipartFile file, String companyGuid, String email) {
        Company company = companyService.findByGuid(companyGuid);
        String storeName = UUID.randomUUID().toString();
        User uploader = userService.findByEmail(email);

        company.setLogo(storeName);
        company.setUpdatedAt(ZonedDateTime.now());
        company.setUpdatedBy(uploader.getId());
        companyRepository.save(company);

        return store(file, logoStorageLocation, storeName);
    }

    public String storeBanner(MultipartFile file, String companyGuid, String email) {
        Company company = companyService.findByGuid(companyGuid);
        String storeName = UUID.randomUUID().toString();
        User uploader = userService.findByEmail(email);

        company.setBanner(storeName);
        company.setUpdatedAt(ZonedDateTime.now());
        company.setUpdatedBy(uploader.getId());
        companyRepository.save(company);

        return store(file, bannerStorageLocation, storeName);
    }

    public String storeLocation(MultipartFile file, String companyGuid, String name, String address, String email) {
        Company company = companyService.findByGuid(companyGuid);
        String storeName = UUID.randomUUID().toString();
        User uploader = userService.findByEmail(email);

        Location location = Location.builder()
                .company(company)
                .name(name)
                .address(address)
                .photo(storeName)
                .guid(UUID.randomUUID().toString())
                .isDeleted(false)
                .createdAt(ZonedDateTime.now())
                .createdBy(uploader.getId())
                .build();

        locationRepository.save(location);

        return store(file, locationStorageLocation, storeName);
    }

    @Override
    public String storeResume(MultipartFile file, String userGuid, String name, String email) {
        User user = userService.findByGuid(userGuid);
        User uploader = userService.findByEmail(email);
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String storeName = UUID.randomUUID().toString();
        String finalFileName = storeName + ".pdf";

        if (!isValidExtension(fileExtension, resumeStorageLocation)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type. Only PDF, DOC, and DOCX are supported for resumes");
        }

        try {
            Files.createDirectories(resumeStorageLocation);

            Path targetLocation = resumeStorageLocation.resolve(finalFileName);

            if (fileExtension.equalsIgnoreCase(".doc") || fileExtension.equalsIgnoreCase(".docx")) {
                WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file.getInputStream());
                try (OutputStream os = new FileOutputStream(targetLocation.toFile())) {
                    Docx4J.toPDF(wordMLPackage, os);
                    os.flush();
                }
            }
            else {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            Resume resume = Resume.builder()
                    .user(user)
                    .name(name)
                    .filePath(storeName)
                    .guid(UUID.randomUUID().toString())
                    .isDeleted(false)
                    .createdAt(ZonedDateTime.now())
                    .createdBy(uploader.getId())
                    .build();

            resumeRepository.save(resume);

            return storeName;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store or convert file " + originalFileName + ". Please try again.", e);
        }
    }

    private String store(MultipartFile file, Path location, String storeName) {
        try {
            Files.createDirectories(imageStorageLocation);
            Files.createDirectories(logoStorageLocation);
            Files.createDirectories(bannerStorageLocation);
            Files.createDirectories(locationStorageLocation);
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
        if (location.equals(imageStorageLocation) || location.equals(logoStorageLocation)
                || location.equals(bannerStorageLocation) || location.equals(locationStorageLocation)) {
            return fileExtension.equalsIgnoreCase(".jpg") ||
                    fileExtension.equalsIgnoreCase(".jpeg") ||
                    fileExtension.equalsIgnoreCase(".png");
        }
        else if (location.equals(resumeStorageLocation)) {
            return fileExtension.equalsIgnoreCase(".pdf") ||
                    fileExtension.equalsIgnoreCase(".doc") ||
                    fileExtension.equalsIgnoreCase(".docx");
        }
        return false;
    }
}
