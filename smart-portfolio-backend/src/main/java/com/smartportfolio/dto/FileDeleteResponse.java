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
public class FileDeleteResponse {

    private String fileName;
    private Boolean success;
    private String message;
    private LocalDateTime deletedAt;

    public static FileDeleteResponse success(String fileName) {
        return FileDeleteResponse.builder()
                .fileName(fileName)
                .success(true)
                .message("Dosya başarıyla silindi")
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static FileDeleteResponse error(String fileName, String message) {
        return FileDeleteResponse.builder()
                .fileName(fileName)
                .success(false)
                .message(message)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}

