package com.smartportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {

    private String fileName;
    private String fileUrl;
    private String originalName;
    private String contentType;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    public static FileUploadResponse success(String fileName, String fileUrl, String originalName, 
                                           String contentType, Long fileSize) {
        return FileUploadResponse.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .originalName(originalName)
                .contentType(contentType)
                .fileSize(fileSize)
                .uploadedAt(LocalDateTime.now())
                .build();
    }
}

