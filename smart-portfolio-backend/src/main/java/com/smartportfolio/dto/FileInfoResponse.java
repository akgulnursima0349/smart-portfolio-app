package com.smartportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileInfoResponse {

    private String fileName;
    private String fileUrl;
    private String contentType;
    private Long fileSize;
    private Boolean exists;
}

