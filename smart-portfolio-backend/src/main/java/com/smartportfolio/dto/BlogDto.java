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
public class BlogDto {

    private Long id;
    private String title;
    private String content;
    private String summary;
    private String imageUrl;
    private Boolean isPublished;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

