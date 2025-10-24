package com.smartportfolio.controller;

import com.smartportfolio.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final AiService aiService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody Map<String, String> body) {
        try {
            String prompt = body.get("prompt");
            
            if (prompt == null || prompt.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Prompt boş olamaz");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            log.info("AI generation request received for prompt: {}", prompt);
            String result = aiService.generateText(prompt);

            Map<String, String> response = new HashMap<>();
            response.put("result", result);

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error in AI controller: ", e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI servisi şu anda kullanılamıyor");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
