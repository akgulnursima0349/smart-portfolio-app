package com.smartportfolio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiService {

    @Value("${groq.api-url}")
    private String apiUrl;

    @Value("${groq.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateText(String prompt) {
        try {
            log.info("AI text generation started for prompt: {}", prompt);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.1-8b-instant");
            requestBody.put("messages", List.of(
                Map.of("role", "user", "content", "Bu proje için kısa ve profesyonel bir açıklama yaz: " + prompt)
            ));
            requestBody.put("max_tokens", 150);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

            if (response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                    String content = (String) message.get("content");
                    
                    if (content != null && !content.trim().isEmpty()) {
                        log.info("AI text generation completed successfully");
                        return content.trim();
                    }
                }
            }

            log.warn("AI service returned empty response");
            return "Metin üretilemedi. Lütfen tekrar deneyin.";
            
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("HTTP Client Error in AI text generation: {}", e.getMessage());
            if (e.getStatusCode().value() == 401) {
                return "AI servisi kimlik doğrulama hatası. API anahtarı geçersiz.";
            } else if (e.getStatusCode().value() == 429) {
                return "AI servisi çok fazla istek aldı. Lütfen birkaç dakika sonra tekrar deneyin.";
            } else if (e.getStatusCode().value() == 404) {
                return "AI servisi model bulunamadı. Lütfen daha sonra tekrar deneyin.";
            } else {
                return "AI servisi HTTP hatası: " + e.getStatusCode().value();
            }
        } catch (org.springframework.web.client.ResourceAccessException e) {
            log.error("Network Error in AI text generation: {}", e.getMessage());
            return "AI servisi ağ hatası. İnternet bağlantınızı kontrol edin.";
        } catch (Exception e) {
            log.error("Unexpected error in AI text generation: ", e);
            return "AI servisi beklenmeyen bir hata aldı. Lütfen daha sonra tekrar deneyin.";
        }
    }
}
