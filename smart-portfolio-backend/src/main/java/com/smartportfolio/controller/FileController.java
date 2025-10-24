package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management", description = "Dosya yükleme ve yönetim işlemleri")
public class FileController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "Dosya yükle", description = "Resim dosyası yükler (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dosya başarıyla yüklendi",
                    content = @Content(schema = @Schema(implementation = FileUploadResponse.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz dosya formatı veya boyutu"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @Parameter(description = "Yüklenecek resim dosyası (JPEG, PNG, GIF, WebP - Max 5MB)", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            log.info("Dosya yükleme işlemi başlatılıyor: {}", file.getOriginalFilename());
            
            // Dosyayı yükle
            String fileName = fileStorageService.uploadImage(file);
            
            // URL oluştur
            String fileUrl = fileStorageService.getImageUrl(fileName);
            
            // Response oluştur
            FileUploadResponse response = FileUploadResponse.success(
                    fileName,
                    fileUrl,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize()
            );
            
            log.info("Dosya başarıyla yüklendi: {} -> {}", file.getOriginalFilename(), fileName);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (Exception e) {
            log.error("Dosya yüklenirken hata oluştu: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Dosya bilgisi", description = "Dosya bilgilerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dosya bilgisi getirildi",
                    content = @Content(schema = @Schema(implementation = FileInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Dosya bulunamadı")
    })
    @GetMapping("/{fileName}")
    public ResponseEntity<FileInfoResponse> getFileInfo(
            @Parameter(description = "Dosya adı", required = true)
            @PathVariable String fileName) {
        try {
            log.info("Dosya bilgisi getiriliyor: {}", fileName);
            
            // Dosyanın var olup olmadığını kontrol et
            boolean exists = fileStorageService.imageExists(fileName);
            
            if (!exists) {
                return ResponseEntity.notFound().build();
            }
            
            // URL oluştur
            String fileUrl = fileStorageService.getImageUrl(fileName);
            
            // Response oluştur
            FileInfoResponse response = FileInfoResponse.builder()
                    .fileName(fileName)
                    .fileUrl(fileUrl)
                    .exists(true)
                    .build();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Dosya bilgisi getirilirken hata oluştu: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Dosya sil", description = "Dosyayı siler (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dosya başarıyla silindi",
                    content = @Content(schema = @Schema(implementation = FileDeleteResponse.class))),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim"),
            @ApiResponse(responseCode = "404", description = "Dosya bulunamadı")
    })
    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FileDeleteResponse> deleteFile(
            @Parameter(description = "Silinecek dosya adı", required = true)
            @PathVariable String fileName) {
        try {
            log.info("Dosya silme işlemi başlatılıyor: {}", fileName);
            
            // Dosyayı sil
            fileStorageService.deleteImage(fileName);
            
            // Response oluştur
            FileDeleteResponse response = FileDeleteResponse.success(fileName);
            
            log.info("Dosya başarıyla silindi: {}", fileName);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Dosya silinirken hata oluştu: {}", e.getMessage());
            
            FileDeleteResponse response = FileDeleteResponse.error(fileName, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Dosya URL'si", description = "Dosyanın erişim URL'sini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dosya URL'si getirildi"),
            @ApiResponse(responseCode = "404", description = "Dosya bulunamadı")
    })
    @GetMapping("/{fileName}/url")
    public ResponseEntity<String> getFileUrl(
            @Parameter(description = "Dosya adı", required = true)
            @PathVariable String fileName) {
        try {
            log.info("Dosya URL'si getiriliyor: {}", fileName);
            
            // Dosyanın var olup olmadığını kontrol et
            boolean exists = fileStorageService.imageExists(fileName);
            
            if (!exists) {
                return ResponseEntity.notFound().build();
            }
            
            // URL oluştur
            String fileUrl = fileStorageService.getImageUrl(fileName);
            
            return ResponseEntity.ok(fileUrl);
            
        } catch (Exception e) {
            log.error("Dosya URL'si getirilirken hata oluştu: {}", e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Dosya varlık kontrolü", description = "Dosyanın var olup olmadığını kontrol eder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dosya varlık durumu getirildi")
    })
    @GetMapping("/{fileName}/exists")
    public ResponseEntity<Boolean> checkFileExists(
            @Parameter(description = "Dosya adı", required = true)
            @PathVariable String fileName) {
        try {
            log.info("Dosya varlığı kontrol ediliyor: {}", fileName);
            
            boolean exists = fileStorageService.imageExists(fileName);
            
            return ResponseEntity.ok(exists);
            
        } catch (Exception e) {
            log.error("Dosya varlığı kontrol edilirken hata oluştu: {}", e.getMessage());
            return ResponseEntity.ok(false);
        }
    }
}