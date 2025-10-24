package com.smartportfolio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language extends BaseEntity {

    @NotBlank(message = "Dil kodu boş olamaz")
    @Pattern(regexp = "^[a-z]{2}$", message = "Dil kodu 2 karakter olmalıdır (örn: en, tr, de)")
    @Size(min = 2, max = 2, message = "Dil kodu tam 2 karakter olmalıdır")
    @Column(nullable = false, unique = true, length = 2)
    private String code;

    @NotBlank(message = "Dil adı boş olamaz")
    @Size(max = 100, message = "Dil adı en fazla 100 karakter olabilir")
    @Column(nullable = false, length = 100)
    private String name;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
}

