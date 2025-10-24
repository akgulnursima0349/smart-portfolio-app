package com.smartportfolio.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogUpdateRequest {

    @Size(max = 200, message = "Blog başlığı en fazla 200 karakter olabilir")
    private String title;

    @Size(max = 10000, message = "Blog içeriği en fazla 10000 karakter olabilir")
    private String content;

    @Size(max = 500, message = "Özet en fazla 500 karakter olabilir")
    private String summary;

    @Size(max = 500, message = "Resim URL'si en fazla 500 karakter olabilir")
    private String imageUrl;

    private Boolean isPublished;
}

