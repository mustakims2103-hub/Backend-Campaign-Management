package com.dt.cotroller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

	
	

	    @Value("${upload.directory}")
	    private String uploadDir;

	    // 🔵 ADMIN FILE UPLOAD
	    @PostMapping("/upload/{campaignId}")
	    public ResponseEntity<String> uploadAdminFiles(@PathVariable String campaignId,
	                                                   @RequestParam("files") MultipartFile[] files) {
	        return uploadFiles(files, "admin", campaignId);
	    }

	    // 🟢 USER FILE UPLOAD
	    @PostMapping("/upload/user/{campaignId}")
	    public ResponseEntity<String> uploadUserFiles(@PathVariable String campaignId,
	                                                  @RequestParam("files") MultipartFile[] files) {
	        return uploadFiles(files, "user", campaignId);
	    }

	    // Shared logic for file saving
	    private ResponseEntity<String> uploadFiles(MultipartFile[] files, String role, String campaignId) {
	        try {
	            Path folderPath = Paths.get(uploadDir, role, campaignId);

	            if (!Files.exists(folderPath)) {
	                Files.createDirectories(folderPath);
	                System.out.println("Created folder: " + folderPath.toString());
	            }

	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    Path filePath = folderPath.resolve(file.getOriginalFilename());
	                    file.transferTo(filePath.toFile());
	                }
	            }

	            return ResponseEntity.ok("Files uploaded successfully");
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("File upload failed: " + e.getMessage());
	        }
	    }

	    // 🔵 ADMIN FILE LIST
	    @GetMapping("/files/{campaignId}")
	    public ResponseEntity<List<String>> listAdminFiles(@PathVariable String campaignId) {
	        return listFiles("admin", campaignId);
	    }

	    // 🟢 USER FILE LIST
	    @GetMapping("/files/user/{campaignId}")
	    public ResponseEntity<List<String>> listUserFiles(@PathVariable String campaignId) {
	        return listFiles("user", campaignId);
	    }

	    private ResponseEntity<List<String>> listFiles(String role, String campaignId) {
	        try {
	            Path folderPath = Paths.get(uploadDir, role, campaignId);
	            if (!Files.exists(folderPath)) {
	                return ResponseEntity.ok(Collections.emptyList());
	            }

	            List<String> fileNames = Files.list(folderPath)
	                    .map(path -> path.getFileName().toString())
	                    .collect(Collectors.toList());

	            return ResponseEntity.ok(fileNames);
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	    // ✅ DOWNLOAD FILE (admin/user-agnostic)
	    @GetMapping("/download/{campaignId}/{filename:.+}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable String campaignId,
	                                                 @PathVariable String filename) {
	        try {
	            Path filePath = findFileAcrossRoles(campaignId, filename);
	            if (filePath == null) return ResponseEntity.notFound().build();

	            Resource resource = new UrlResource(filePath.toUri());
	            if (!resource.exists() || !resource.isReadable()) {
	                return ResponseEntity.notFound().build();
	            }

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION,
	                            "attachment; filename=\"" + resource.getFilename() + "\"")
	                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                    .body(resource);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	    // ✅ DELETE FILE (admin/user-agnostic)
	    @DeleteMapping("/delete/{campaignId}/{filename:.+}")
	    public ResponseEntity<String> deleteFile(@PathVariable String campaignId,
	                                             @PathVariable String filename) {
	        try {
	            Path filePath = findFileAcrossRoles(campaignId, filename);
	            if (filePath == null || !Files.exists(filePath)) {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                        .body("File not found");
	            }

	            Files.delete(filePath);
	            return ResponseEntity.ok("File deleted successfully");
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to delete file: " + e.getMessage());
	        }
	    }

	    // 🔎 Helper: Search file in both admin and user folders
	    private Path findFileAcrossRoles(String campaignId, String filename) {
	        Path adminPath = Paths.get(uploadDir, "admin", campaignId, filename);
	        if (Files.exists(adminPath)) return adminPath;

	        Path userPath = Paths.get(uploadDir, "user", campaignId, filename);
	        if (Files.exists(userPath)) return userPath;

	        return null;
	    }
	}


