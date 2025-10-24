package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Kimlik doğrulama ve yetkilendirme işlemleri")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Kullanıcı kaydı", description = "Yeni kullanıcı hesabı oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla kaydedildi",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi"),
            @ApiResponse(responseCode = "409", description = "Kullanıcı adı veya email zaten mevcut")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Kullanıcı kayıt bilgileri", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı/email ve şifre ile giriş yapar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Giriş başarılı",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Geçersiz kimlik bilgileri"),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Giriş bilgileri", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mevcut kullanıcı bilgisi", description = "Token ile mevcut kullanıcının bilgilerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı bilgileri getirildi",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Geçersiz token"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
            @Parameter(description = "JWT Bearer token", required = true)
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        UserDto userDto = authService.getCurrentUser(jwtToken);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Kullanıcı çıkışı", description = "Token'ları geçersiz kılar ve çıkış yapar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarıyla çıkış yapıldı",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "401", description = "Geçersiz token"),
            @ApiResponse(responseCode = "400", description = "Çıkış işlemi başarısız")
    })
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(
            @Parameter(description = "JWT Bearer token", required = true)
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Çıkış isteği (opsiyonel)")
            @RequestBody(required = false) LogoutRequest logoutRequest) {
        String accessToken = authHeader.replace("Bearer ", "");
        String refreshToken = (logoutRequest != null && logoutRequest.getRefreshToken() != null) 
                ? logoutRequest.getRefreshToken() 
                : null;
        
        MessageResponse response = authService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Token yenileme", description = "Refresh token ile yeni access token oluşturur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token başarıyla yenilendi",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Geçersiz refresh token"),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @Parameter(description = "Refresh token bilgisi", required = true)
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse response = authService.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}

