package com.smartportfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog extends BaseEntity {

    @NotBlank(message = "Blog başlığı boş olamaz")
    @Size(max = 200, message = "Blog başlığı en fazla 200 karakter olabilir")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Blog içeriği boş olamaz")
    @Size(max = 10000, message = "Blog içeriği en fazla 10000 karakter olabilir")
    @Column(nullable = false, length = 10000)
    private String content;

    @Size(max = 500, message = "Özet en fazla 500 karakter olabilir")
    @Column(length = 500)
    private String summary;

    @Size(max = 500, message = "Resim URL'si en fazla 500 karakter olabilir")
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Builder.Default
    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Builder.Default
    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;
}

