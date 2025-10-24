package com.smartportfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String BLACKLIST_PREFIX = "blacklist:token:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh:token:";

    /**
     * Token'ı blacklist'e ekler
     */
    public void blacklistToken(String token, long expirationTimeInMs) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(key, "blacklisted", expirationTimeInMs, TimeUnit.MILLISECONDS);
            log.info("Token blacklist'e eklendi");
        } catch (Exception e) {
            log.error("Token blacklist'e eklenirken hata oluştu: {}", e.getMessage());
        }
    }

    /**
     * Token'ın blacklist'te olup olmadığını kontrol eder
     */
    public boolean isTokenBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Token blacklist kontrolü sırasında hata oluştu: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Refresh token'ı Redis'e kaydeder
     */
    public void saveRefreshToken(Long userId, String refreshToken, long expirationTimeInMs) {
        try {
            String key = REFRESH_TOKEN_PREFIX + userId;
            redisTemplate.opsForValue().set(key, refreshToken, expirationTimeInMs, TimeUnit.MILLISECONDS);
            log.info("Refresh token Redis'e kaydedildi - User ID: {}", userId);
        } catch (Exception e) {
            log.error("Refresh token kaydedilirken hata oluştu: {}", e.getMessage());
        }
    }

    /**
     * Kullanıcının refresh token'ını getirir
     */
    public String getRefreshToken(Long userId) {
        try {
            String key = REFRESH_TOKEN_PREFIX + userId;
            Object token = redisTemplate.opsForValue().get(key);
            return token != null ? token.toString() : null;
        } catch (Exception e) {
            log.error("Refresh token getirilirken hata oluştu: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Kullanıcının refresh token'ını siler
     */
    public void deleteRefreshToken(Long userId) {
        try {
            String key = REFRESH_TOKEN_PREFIX + userId;
            redisTemplate.delete(key);
            log.info("Refresh token silindi - User ID: {}", userId);
        } catch (Exception e) {
            log.error("Refresh token silinirken hata oluştu: {}", e.getMessage());
        }
    }

    /**
     * Token'ı blacklist'ten kaldırır (test için)
     */
    public void removeFromBlacklist(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.delete(key);
            log.info("Token blacklist'ten kaldırıldı");
        } catch (Exception e) {
            log.error("Token blacklist'ten kaldırılırken hata oluştu: {}", e.getMessage());
        }
    }
}


