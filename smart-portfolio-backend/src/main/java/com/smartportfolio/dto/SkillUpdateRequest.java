package com.smartportfolio.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillUpdateRequest {

    @Size(max = 100, message = "Yetenek adı en fazla 100 karakter olabilir")
    private String name;

    private Integer level;

    @Size(max = 100, message = "Kategori en fazla 100 karakter olabilir")
    private String category;

    @Size(max = 500, message = "İkon URL'si en fazla 500 karakter olabilir")
    private String iconUrl;

    private Boolean isActive;
    private Integer sortOrder;
}

