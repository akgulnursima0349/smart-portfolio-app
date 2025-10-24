package com.smartportfolio.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageUpdateRequest {

    @Size(max = 100, message = "Dil adı en fazla 100 karakter olabilir")
    private String name;

    private Boolean isActive;
    private Boolean isDefault;
}

