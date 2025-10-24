package com.smartportfolio.service;

import com.smartportfolio.dto.*;
import com.smartportfolio.exception.*;
import com.smartportfolio.model.Role;
import com.smartportfolio.model.User;
import com.smartportfolio.repository.RoleRepository;
import com.smartportfolio.repository.UserRepository;
import com.smartportfolio.security.JwtTokenProvider;
import com.smartportfolio.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final RedisTokenService redisTokenService;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Yeni kullanıcı kaydı başlatılıyor: {}", registerRequest.getUsername());

        // Kullanıcı adı kontrolü
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(registerRequest.getUsername());
        }

        // Email kontrolü
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException(registerRequest.getEmail());
        }

        // User oluştur
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .isActive(true)
                .isEmailVerified(false)
                .build();

        // Varsayılan rol ata (USER)
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name("USER")
                            .description("Default user role")
                            .permissions(new HashSet<>())
                            .build();
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.info("Kullanıcı başarıyla kaydedildi: {}", savedUser.getUsername());

        // Token oluştur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getUsername(),
                        registerRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // Refresh token'ı Redis'e kaydet
        redisTokenService.saveRefreshToken(savedUser.getId(), refreshToken, 604800000L); // 7 gün

        UserDto userDto = modelMapper.map(savedUser, UserDto.class);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(userDto)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Kullanıcı girişi yapılıyor: {}", loginRequest.getUsernameOrEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // Kullanıcı bilgilerini getir
        User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getUsernameOrEmail()
        ).orElseThrow(() -> new ResourceNotFoundException(
                "User", "usernameOrEmail", loginRequest.getUsernameOrEmail()
        ));

        // Refresh token'ı Redis'e kaydet
        redisTokenService.saveRefreshToken(user.getId(), refreshToken, 604800000L); // 7 gün

        // Son giriş zamanını güncelle
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        log.info("Kullanıcı başarıyla giriş yaptı: {}", user.getUsername());

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(userDto)
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUser(String token) {
        Long userId = tokenProvider.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public MessageResponse logout(String accessToken, String refreshToken) {
        log.info("Kullanıcı çıkış yapıyor");

        try {
            // Access token'dan kullanıcı ID'sini al
            Long userId = tokenProvider.getUserIdFromToken(accessToken);

            // Access token'ı blacklist'e ekle (kalan süre kadar)
            long remainingTime = tokenProvider.getRemainingExpirationTime(accessToken);
            if (remainingTime > 0) {
                redisTokenService.blacklistToken(accessToken, remainingTime);
            }

            // Refresh token'ı Redis'ten sil
            redisTokenService.deleteRefreshToken(userId);

            log.info("Kullanıcı başarıyla çıkış yaptı - User ID: {}", userId);
            return MessageResponse.success("Başarıyla çıkış yapıldı");
        } catch (Exception e) {
            log.error("Logout sırasında hata oluştu: {}", e.getMessage());
            throw new BadRequestException("Çıkış işlemi sırasında bir hata oluştu");
        }
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Token yenileme işlemi başlatılıyor");

        // Token geçerliliğini kontrol et
        if (!tokenProvider.validateToken(refreshToken)) {
            log.error("Geçersiz refresh token");
            throw new InvalidTokenException("Geçersiz refresh token");
        }

        // Token'dan kullanıcı ID'sini al
        Long userId = tokenProvider.getUserIdFromToken(refreshToken);

        // Redis'teki refresh token ile karşılaştır
        String storedRefreshToken = redisTokenService.getRefreshToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            log.error("Refresh token eşleşmiyor veya bulunamadı - User ID: {}", userId);
            throw new InvalidTokenException("Refresh token geçersiz veya süresi dolmuş");
        }

        // Kullanıcıyı getir
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Yeni token'lar oluştur
        String newAccessToken = tokenProvider.generateTokenFromUserId(userId);
        String newRefreshToken = tokenProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(
                        UserPrincipal.create(user), 
                        null, 
                        UserPrincipal.create(user).getAuthorities()
                )
        );

        // Yeni refresh token'ı Redis'e kaydet
        redisTokenService.saveRefreshToken(userId, newRefreshToken, 604800000L); // 7 gün

        UserDto userDto = modelMapper.map(user, UserDto.class);

        log.info("Token başarıyla yenilendi - User ID: {}", userId);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L)
                .user(userDto)
                .build();
    }
}

