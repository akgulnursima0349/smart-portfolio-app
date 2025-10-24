package com.smartportfolio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Kullanıcı kayıt isteği")
public class RegisterRequest {

    @Schema(description = "Kullanıcı adı", example = "johndoe", required = true)
    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 100, message = "Kullanıcı adı 3-100 karakter arasında olmalıdır")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Kullanıcı adı sadece harf, rakam, nokta, alt çizgi ve tire içerebilir")
    private String username;

    @Schema(description = "Email adresi", example = "john@example.com", required = true)
    @NotBlank(message = "Email boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Size(max = 150, message = "Email en fazla 150 karakter olabilir")
    private String email;

    @Schema(description = "Şifre (en az 8 karakter, büyük harf, küçük harf, rakam ve özel karakter)", example = "SecurePass123!", required = true)
    @NotBlank(message = "Şifre boş olamaz")
    @Size(min = 8, max = 100, message = "Şifre 8-100 karakter arasında olmalıdır")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message = "Şifre en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter içermelidir")
    private String password;

    @Schema(description = "Ad", example = "John", required = true)
    @NotBlank(message = "Ad boş olamaz")
    @Size(max = 100, message = "Ad en fazla 100 karakter olabilir")
    private String firstName;

    @Schema(description = "Soyad", example = "Doe", required = true)
    @NotBlank(message = "Soyad boş olamaz")
    @Size(max = 100, message = "Soyad en fazla 100 karakter olabilir")
    private String lastName;

    @Schema(description = "Telefon numarası", example = "+905551234567")
    @Pattern(regexp = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$", message = "Geçerli bir telefon numarası giriniz")
    private String phoneNumber;

}

