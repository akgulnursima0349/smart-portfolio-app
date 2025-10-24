package com.smartportfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Kullanıcı giriş isteği")
public class LoginRequest {

    @Schema(description = "Kullanıcı adı veya email", example = "johndoe", required = true)
    @NotBlank(message = "Kullanıcı adı veya email boş olamaz")
    private String usernameOrEmail;

    @Schema(description = "Şifre", example = "SecurePass123!", required = true)
    @NotBlank(message = "Şifre boş olamaz")
    private String password;
}

