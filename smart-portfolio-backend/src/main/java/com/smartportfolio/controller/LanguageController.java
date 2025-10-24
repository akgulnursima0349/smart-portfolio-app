package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LanguageDto>> searchLanguages(@RequestParam String keyword) {
        List<LanguageDto> languages = languageService.searchLanguages(keyword);
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> getLanguageById(@PathVariable Long id) {
        LanguageDto language = languageService.getLanguageById(id);
        return ResponseEntity.ok(language);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<LanguageDto> getLanguageByCode(@PathVariable String code) {
        LanguageDto language = languageService.getLanguageByCode(code);
        return ResponseEntity.ok(language);
    }

    @GetMapping("/default")
    public ResponseEntity<LanguageDto> getDefaultLanguage() {
        LanguageDto language = languageService.getDefaultLanguage();
        return ResponseEntity.ok(language);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageDto> createLanguage(@Valid @RequestBody LanguageCreateRequest request) {
        LanguageDto language = languageService.createLanguage(request);
        return new ResponseEntity<>(language, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LanguageDto> updateLanguage(
            @PathVariable Long id,
            @Valid @RequestBody LanguageUpdateRequest request) {
        LanguageDto language = languageService.updateLanguage(id, request);
        return ResponseEntity.ok(language);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getLanguageCount() {
        long count = languageService.getLanguageCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/default-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getDefaultLanguageCount() {
        long count = languageService.getDefaultLanguageCount();
        return ResponseEntity.ok(count);
    }
}

