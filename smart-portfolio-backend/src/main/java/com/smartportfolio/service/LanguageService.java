package com.smartportfolio.service;

import com.smartportfolio.dto.*;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.Language;
import com.smartportfolio.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageService {

    private final LanguageRepository languageRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<LanguageDto> getAllLanguages() {
        log.info("Tüm aktif diller getiriliyor");
        List<Language> languages = languageRepository.findByIsActiveTrueOrderByNameAsc();
        return languages.stream()
                .map(language -> modelMapper.map(language, LanguageDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LanguageDto> searchLanguages(String keyword) {
        log.info("Diller aranıyor - keyword: {}", keyword);
        List<Language> languages = languageRepository.findByKeywordAndIsActiveTrue(keyword);
        return languages.stream()
                .map(language -> modelMapper.map(language, LanguageDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LanguageDto getLanguageById(Long id) {
        log.info("Dil getiriliyor - ID: {}", id);
        Language language = languageRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language", "id", id));
        return modelMapper.map(language, LanguageDto.class);
    }

    @Transactional(readOnly = true)
    public LanguageDto getLanguageByCode(String code) {
        log.info("Dil getiriliyor - Code: {}", code);
        Language language = languageRepository.findByCodeIgnoreCaseAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language", "code", code));
        return modelMapper.map(language, LanguageDto.class);
    }

    @Transactional(readOnly = true)
    public LanguageDto getDefaultLanguage() {
        log.info("Varsayılan dil getiriliyor");
        Language language = languageRepository.findByIsDefaultTrueAndIsActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Default Language", "isDefault", true));
        return modelMapper.map(language, LanguageDto.class);
    }

    @Transactional
    public LanguageDto createLanguage(LanguageCreateRequest request) {
        log.info("Yeni dil oluşturuluyor: {} ({})", request.getName(), request.getCode());
        
        // Aynı kod veya isimde dil var mı kontrol et
        if (languageRepository.existsByCodeIgnoreCaseAndIsActiveTrue(request.getCode())) {
            throw new IllegalArgumentException("Bu kodda bir dil zaten mevcut: " + request.getCode());
        }
        if (languageRepository.existsByNameIgnoreCaseAndIsActiveTrue(request.getName())) {
            throw new IllegalArgumentException("Bu isimde bir dil zaten mevcut: " + request.getName());
        }
        
        // Eğer varsayılan dil olarak işaretleniyorsa, diğer dilleri varsayılan olmaktan çıkar
        if (request.getIsDefault() != null && request.getIsDefault()) {
            List<Language> defaultLanguages = languageRepository.findAll().stream()
                    .filter(lang -> lang.getIsDefault() && lang.getIsActive())
                    .toList();
            for (Language lang : defaultLanguages) {
                lang.setIsDefault(false);
                languageRepository.save(lang);
            }
        }
        
        Language language = Language.builder()
                .code(request.getCode().toLowerCase())
                .name(request.getName())
                .isDefault(request.getIsDefault() != null ? request.getIsDefault() : false)
                .isActive(true)
                .build();

        Language savedLanguage = languageRepository.save(language);
        log.info("Dil başarıyla oluşturuldu - ID: {}", savedLanguage.getId());
        
        return modelMapper.map(savedLanguage, LanguageDto.class);
    }

    @Transactional
    public LanguageDto updateLanguage(Long id, LanguageUpdateRequest request) {
        log.info("Dil güncelleniyor - ID: {}", id);
        
        Language language = languageRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language", "id", id));

        if (request.getName() != null) {
            // Aynı isimde başka dil var mı kontrol et
            if (!language.getName().equalsIgnoreCase(request.getName()) && 
                languageRepository.existsByNameIgnoreCaseAndIsActiveTrue(request.getName())) {
                throw new IllegalArgumentException("Bu isimde bir dil zaten mevcut: " + request.getName());
            }
            language.setName(request.getName());
        }
        if (request.getIsActive() != null) language.setIsActive(request.getIsActive());
        if (request.getIsDefault() != null) {
            // Eğer varsayılan dil olarak işaretleniyorsa, diğer dilleri varsayılan olmaktan çıkar
            if (request.getIsDefault()) {
                List<Language> defaultLanguages = languageRepository.findAll().stream()
                        .filter(lang -> lang.getIsDefault() && lang.getIsActive() && !lang.getId().equals(id))
                        .toList();
                for (Language lang : defaultLanguages) {
                    lang.setIsDefault(false);
                    languageRepository.save(lang);
                }
            }
            language.setIsDefault(request.getIsDefault());
        }

        Language updatedLanguage = languageRepository.save(language);
        log.info("Dil başarıyla güncellendi - ID: {}", id);
        
        return modelMapper.map(updatedLanguage, LanguageDto.class);
    }

    @Transactional
    public void deleteLanguage(Long id) {
        log.info("Dil siliniyor - ID: {}", id);
        
        Language language = languageRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language", "id", id));
        
        language.setIsActive(false);
        languageRepository.save(language);
        
        log.info("Dil başarıyla silindi - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long getLanguageCount() {
        return languageRepository.countByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public long getDefaultLanguageCount() {
        return languageRepository.countByIsDefaultTrueAndIsActiveTrue();
    }
}

