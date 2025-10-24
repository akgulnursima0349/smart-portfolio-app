package com.smartportfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Kimlik doğrulama yanıtı")
public class AuthResponse {

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String refreshToken;

    @Schema(description = "Token tipi", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token süresi (saniye)", example = "86400")
    private Long expiresIn;

    @Schema(description = "Kullanıcı bilgileri")
    private UserDto user;
}

