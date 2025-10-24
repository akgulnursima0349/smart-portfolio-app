package com.smartportfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @NotBlank(message = "Proje başlığı boş olamaz")
    @Size(max = 200, message = "Proje başlığı en fazla 200 karakter olabilir")
    @Column(nullable = false, length = 200)
    private String title;

    @Size(max = 2000, message = "Proje açıklaması en fazla 2000 karakter olabilir")
    @Column(length = 2000)
    private String description;

    @Size(max = 500, message = "Resim URL'si en fazla 500 karakter olabilir")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Size(max = 500, message = "GitHub URL'si en fazla 500 karakter olabilir")
    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Size(max = 500, message = "Demo URL'si en fazla 500 karakter olabilir")
    @Column(name = "demo_url", length = 500)
    private String demoUrl;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;
}

