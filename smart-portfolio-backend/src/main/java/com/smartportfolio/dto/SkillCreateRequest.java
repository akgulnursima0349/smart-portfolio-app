package com.smartportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillCreateRequest {

    @NotBlank(message = "Yetenek adı boş olamaz")
    @Size(max = 100, message = "Yetenek adı en fazla 100 karakter olabilir")
    private String name;

    @NotNull(message = "Yetenek seviyesi seçilmelidir")
    private Integer level;

    @Size(max = 100, message = "Kategori en fazla 100 karakter olabilir")
    private String category;

    @Size(max = 500, message = "İkon URL'si en fazla 500 karakter olabilir")
    private String iconUrl;

    private Integer sortOrder;
}

