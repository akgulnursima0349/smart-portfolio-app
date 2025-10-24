package com.smartportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCreateRequest {

    @NotBlank(message = "Proje başlığı boş olamaz")
    @Size(max = 200, message = "Proje başlığı en fazla 200 karakter olabilir")
    private String title;

    @Size(max = 2000, message = "Proje açıklaması en fazla 2000 karakter olabilir")
    private String description;

    @Size(max = 500, message = "Resim URL'si en fazla 500 karakter olabilir")
    private String imageUrl;

    @Size(max = 500, message = "GitHub URL'si en fazla 500 karakter olabilir")
    private String githubUrl;

    @Size(max = 500, message = "Demo URL'si en fazla 500 karakter olabilir")
    private String demoUrl;

    private Boolean isFeatured;
}

