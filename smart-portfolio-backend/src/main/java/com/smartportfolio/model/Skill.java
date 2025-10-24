package com.smartportfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill extends BaseEntity {

    @NotBlank(message = "Yetenek adı boş olamaz")
    @Size(max = 100, message = "Yetenek adı en fazla 100 karakter olabilir")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "Yetenek seviyesi seçilmelidir")
    @Column(nullable = false)
    private Integer level;

    @Size(max = 100, message = "Kategori en fazla 100 karakter olabilir")
    @Column(length = 100)
    private String category;

    @Size(max = 500, message = "İkon URL'si en fazla 500 karakter olabilir")
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}

