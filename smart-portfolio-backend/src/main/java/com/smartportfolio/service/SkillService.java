package com.smartportfolio.service;

import com.smartportfolio.dto.*;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.Skill;
import com.smartportfolio.repository.SkillRepository;
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
public class SkillService {

    private final SkillRepository skillRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<SkillDto> getAllSkills() {
        log.info("Tüm aktif yetenekler getiriliyor");
        List<Skill> skills = skillRepository.findByIsActiveTrueOrderBySortOrderAscNameAsc();
        return skills.stream()
                .map(skill -> modelMapper.map(skill, SkillDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillDto> getSkillsByLevel(Integer level) {
        log.info("Yetenekler seviyeye göre getiriliyor - Level: {}", level);
        List<Skill> skills = skillRepository.findByLevelAndIsActiveTrueOrderBySortOrderAscNameAsc(level);
        return skills.stream()
                .map(skill -> modelMapper.map(skill, SkillDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SkillDto> searchSkills(String keyword) {
        log.info("Yetenekler aranıyor - keyword: {}", keyword);
        List<Skill> skills = skillRepository.findByKeywordAndIsActiveTrue(keyword);
        return skills.stream()
                .map(skill -> modelMapper.map(skill, SkillDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SkillDto getSkillById(Long id) {
        log.info("Yetenek getiriliyor - ID: {}", id);
        Skill skill = skillRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        return modelMapper.map(skill, SkillDto.class);
    }

    @Transactional(readOnly = true)
    public SkillDto getSkillByName(String name) {
        log.info("Yetenek getiriliyor - Name: {}", name);
        Skill skill = skillRepository.findByNameIgnoreCaseAndIsActiveTrue(name)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "name", name));
        return modelMapper.map(skill, SkillDto.class);
    }

    @Transactional
    public SkillDto createSkill(SkillCreateRequest request) {
        log.info("Yeni yetenek oluşturuluyor: {}", request.getName());
        
        // Aynı isimde yetenek var mı kontrol et
        if (skillRepository.existsByNameIgnoreCaseAndIsActiveTrue(request.getName())) {
            throw new IllegalArgumentException("Bu isimde bir yetenek zaten mevcut: " + request.getName());
        }
        
        Skill skill = Skill.builder()
                .name(request.getName())
                .level(request.getLevel())
                .category(request.getCategory())
                .iconUrl(request.getIconUrl())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .isActive(true)
                .build();

        Skill savedSkill = skillRepository.save(skill);
        log.info("Yetenek başarıyla oluşturuldu - ID: {}", savedSkill.getId());
        
        return modelMapper.map(savedSkill, SkillDto.class);
    }

    @Transactional
    public SkillDto updateSkill(Long id, SkillUpdateRequest request) {
        log.info("Yetenek güncelleniyor - ID: {}", id);
        
        Skill skill = skillRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));

        if (request.getName() != null) {
            // Aynı isimde başka yetenek var mı kontrol et
            if (!skill.getName().equalsIgnoreCase(request.getName()) && 
                skillRepository.existsByNameIgnoreCaseAndIsActiveTrue(request.getName())) {
                throw new IllegalArgumentException("Bu isimde bir yetenek zaten mevcut: " + request.getName());
            }
            skill.setName(request.getName());
        }
        if (request.getLevel() != null) skill.setLevel(request.getLevel());
        if (request.getCategory() != null) skill.setCategory(request.getCategory());
        if (request.getIconUrl() != null) skill.setIconUrl(request.getIconUrl());
        if (request.getIsActive() != null) skill.setIsActive(request.getIsActive());
        if (request.getSortOrder() != null) skill.setSortOrder(request.getSortOrder());

        Skill updatedSkill = skillRepository.save(skill);
        log.info("Yetenek başarıyla güncellendi - ID: {}", id);
        
        return modelMapper.map(updatedSkill, SkillDto.class);
    }

    @Transactional
    public void deleteSkill(Long id) {
        log.info("Yetenek siliniyor - ID: {}", id);
        
        Skill skill = skillRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", id));
        
        skill.setIsActive(false);
        skillRepository.save(skill);
        
        log.info("Yetenek başarıyla silindi - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long getSkillCount() {
        return skillRepository.countByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public long getSkillCountByLevel(Integer level) {
        return skillRepository.countByLevelAndIsActiveTrue(level);
    }

    @Transactional(readOnly = true)
    public List<Object[]> getSkillCountByLevel() {
        return skillRepository.getSkillCountByLevel();
    }
}

