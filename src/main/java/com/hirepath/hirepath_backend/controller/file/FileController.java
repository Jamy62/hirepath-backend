package com.hirepath.hirepath_backend.controller.file;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;
    private final Path imageStorageLocation = Paths.get("files/images").toAbsolutePath().normalize();
    private final Path logoStorageLocation = Paths.get("files/logos").toAbsolutePath().normalize();
    private final Path bannerStorageLocation = Paths.get("files/banners").toAbsolutePath().normalize();
    private final Path locationStorageLocation = Paths.get("files/locations").toAbsolutePath().normalize();
    private final Path resumeStorageLocation = Paths.get("files/resumes").toAbsolutePath().normalize();

    @PostMapping("/upload/{type}")
    public ResponseEntity<ResponseFormat> fileUpload(@PathVariable String type,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam(required = false) String userGuid,
                                                     @RequestParam(required = false) String companyGuid,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String address,
                                                     Principal principal) {
        String fileName;
        switch (type) {
            case "profile":
                if (userGuid == null || userGuid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userGuid is required for profile image uploads.");
                }
                fileName = fileStorageService.storeProfile(file, userGuid, principal.getName());
                break;

            case "logo":
                if (companyGuid == null || companyGuid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyGuid is required for company logo uploads.");
                }
                fileName = fileStorageService.storeLogo(file, companyGuid, principal.getName());
                break;

            case "banner":
                if (companyGuid == null || companyGuid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyGuid is required for company banner uploads.");
                }
                fileName = fileStorageService.storeBanner(file, companyGuid, principal.getName());
                break;

            case "location":
                if (companyGuid == null || companyGuid.isBlank() || name == null || name.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyGuid, name, and address is required for company location uploads.");
                }
                fileName = fileStorageService.storeLocation(file, companyGuid, name, address, principal.getName());
                break;

            case "resume":
                if (userGuid == null || userGuid.isBlank() || name == null || name.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userGuid and name is required for resume uploads.");
                }
                fileName = fileStorageService.storeResume(file, userGuid, name, principal.getName());
                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type specified.");
        }

        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(fileName, "File uploaded successfully."));
    }

    @DeleteMapping("/delete/{type}/{guid}")
    public ResponseEntity<ResponseFormat> deleteFile(@PathVariable String type,
                                                     @PathVariable String guid,
                                                     Principal principal) {
        switch (type) {
            case "resume":
                if (guid == null || guid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "guid is required for resume deletion.");
                }
                fileStorageService.deleteResume(guid, principal.getName());
                break;
            case "location":
                if (guid == null || guid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "guid is required for location deletion.");
                }
                fileStorageService.deleteLocation(guid, principal.getName());
                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type specified for deletion.");
        }

        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(null, "File deleted successfully."));
    }

    @GetMapping("/download/{type}/{filename:.+}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String type, @PathVariable String filename) {
        Path location;
        String searchFilename = filename;

        switch (type) {
            case "images":
                location = imageStorageLocation;
                break;
            case "logo":
                location = logoStorageLocation;
                break;
            case "banner":
                location = bannerStorageLocation;
                break;
            case "location":
                location = locationStorageLocation;
                break;
            case "resumes":
                location = resumeStorageLocation;
                searchFilename = fileStorageService.getResumeFilenameByGuid(filename);
                break;

            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type specified.");
        }

        if (!Files.exists(location)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
        }

        try (Stream<Path> files = Files.list(location)) {
            String finalSearchFilename = searchFilename;
            Optional<Path> foundFile = files.filter(file -> file.getFileName().toString().startsWith(finalSearchFilename))
                    .findFirst();

            if (foundFile.isPresent()) {
                Path filePath = foundFile.get();
                Resource resource = new UrlResource(filePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    String actualFilename = filePath.getFileName().toString();
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + actualFilename + "\"")
                            .contentType(getMediaTypeForFileName(actualFilename))
                            .body(resource);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + filename);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + filename);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR ,"Error downloading file " + filename, e);
        }
    }

    private MediaType getMediaTypeForFileName(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        switch (extension.toLowerCase()) {
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "doc":
                return MediaType.valueOf("application/msword");
            case "docx":
                return MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "jpg":
            case "jpeg":
            default:
                return MediaType.IMAGE_JPEG;
        }
    }
}
