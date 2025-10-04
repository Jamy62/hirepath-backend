package com.hirepath.hirepath_backend.controller.file;

import com.hirepath.hirepath_backend.model.response.ResponseFormat;
import com.hirepath.hirepath_backend.service.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;
    private final Path imageStorageLocation = Paths.get("files/images").toAbsolutePath().normalize();
    private final Path resumeStorageLocation = Paths.get("files/resumes").toAbsolutePath().normalize();

    @PostMapping("/upload/{type}")
    public ResponseEntity<ResponseFormat> uploadFile(@PathVariable String type,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam(required = false) String userGuid,
                                                     @RequestParam(required = false) String resumeGuid) {
        String fileName;
        switch (type) {
            case "profile":
                if (userGuid == null || userGuid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userGuid is required for profile image uploads.");
                }
                fileName = fileStorageService.storeProfile(file, userGuid);
                break;
            case "resume":
                if (resumeGuid == null || resumeGuid.isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "resumeGuid is required for resume uploads.");
                }
                fileName = fileStorageService.storeResume(file, resumeGuid);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type specified.");
        }
        return ResponseEntity.ok(ResponseFormat.createSuccessResponse(fileName, "File uploaded successfully."));
    }

    @GetMapping("/download/{type}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String type, @PathVariable String filename) {
        Path location;
        switch (type) {
            case "images":
                location = imageStorageLocation;
                break;
            case "resumes":
                location = resumeStorageLocation;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file type specified.");
        }

//        try {
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
//                        .contentType(getMediaTypeForFileName(filename))
//                        .body(resource);
//            } else {
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + filename);
//            }

        try (Stream<Path> files = Files.list(location)) {
            Optional<Path> foundFile = files.filter(file -> file.getFileName().toString().startsWith(filename))
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
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"File not found " + filename, e);
        }
        return null;
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
