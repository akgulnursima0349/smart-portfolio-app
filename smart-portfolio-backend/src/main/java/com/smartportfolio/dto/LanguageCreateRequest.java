package com.smartportfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageCreateRequest {

    @NotBlank(message = "Dil kodu boş olamaz")
    @Pattern(regexp = "^[a-z]{2}$", message = "Dil kodu 2 karakter olmalıdır (örn: en, tr, de)")
    @Size(min = 2, max = 2, message = "Dil kodu tam 2 karakter olmalıdır")
    private String code;

    @NotBlank(message = "Dil adı boş olamaz")
    @Size(max = 100, message = "Dil adı en fazla 100 karakter olabilir")
    private String name;

    private Boolean isDefault;
}

