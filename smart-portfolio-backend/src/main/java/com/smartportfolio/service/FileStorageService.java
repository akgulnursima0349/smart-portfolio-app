package com.smartportfolio.service;

import com.smartportfolio.exception.FileUploadException;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    public String uploadImage(MultipartFile file) {
        try {
            // Bucket'ın var olup olmadığını kontrol et, yoksa oluştur
            ensureBucketExists();

            // Dosya validasyonu
            validateFile(file);

            // Benzersiz dosya adı oluştur
            String fileName = generateUniqueFileName(file.getOriginalFilename());

            // Dosyayı MinIO'ya yükle
            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            }

            log.info("Dosya başarıyla yüklendi: {}", fileName);
            return fileName;

        } catch (Exception e) {
            log.error("Dosya yüklenirken hata oluştu: {}", e.getMessage());
            throw new FileUploadException("Dosya yüklenemedi: " + e.getMessage(), e);
        }
    }

    public String getImageUrl(String fileName) {
        try {
            // Dosyanın var olup olmadığını kontrol et
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

            // Public URL oluştur
            String url = String.format("%s/%s/%s", endpoint, bucketName, fileName);
            log.info("Resim URL'si oluşturuldu: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Resim URL'si oluşturulurken hata oluştu: {}", e.getMessage());
            throw new RuntimeException("Resim URL'si oluşturulamadı: " + e.getMessage(), e);
        }
    }

    public void deleteImage(String fileName) {
        try {
            // Dosyayı sil
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );

            log.info("Dosya başarıyla silindi: {}", fileName);

        } catch (Exception e) {
            log.error("Dosya silinirken hata oluştu: {}", e.getMessage());
            throw new RuntimeException("Dosya silinemedi: " + e.getMessage(), e);
        }
    }

    public boolean imageExists(String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!bucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            log.info("Bucket oluşturuldu: {}", bucketName);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Dosya boş olamaz");
        }

        // Dosya boyutu kontrolü (5MB limit)
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Dosya boyutu 5MB'dan büyük olamaz");
        }

        // Dosya tipi kontrolü
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Sadece resim dosyaları yüklenebilir");
        }

        // Desteklenen formatlar
        String[] allowedTypes = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
        boolean isAllowed = false;
        for (String type : allowedTypes) {
            if (contentType.equals(type)) {
                isAllowed = true;
                break;
            }
        }

        if (!isAllowed) {
            throw new IllegalArgumentException("Desteklenmeyen dosya formatı. Sadece JPEG, PNG, GIF, WebP desteklenir");
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        if (originalFileName != null && !originalFileName.isEmpty()) {
            String extension = getFileExtension(originalFileName);
            return String.format("%s_%s_%s%s", timestamp, uuid, "image", extension);
        }
        
        return String.format("%s_%s_%s.jpg", timestamp, uuid, "image");
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return ".jpg"; // Varsayılan uzantı
    }
}
