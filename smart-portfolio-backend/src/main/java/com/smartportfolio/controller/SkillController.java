package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        List<SkillDto> skills = skillService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<List<SkillDto>> getSkillsByLevel(@PathVariable Integer level) {
        List<SkillDto> skills = skillService.getSkillsByLevel(level);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SkillDto>> searchSkills(@RequestParam String keyword) {
        List<SkillDto> skills = skillService.searchSkills(keyword);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillDto> getSkillById(@PathVariable Long id) {
        SkillDto skill = skillService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SkillDto> getSkillByName(@PathVariable String name) {
        SkillDto skill = skillService.getSkillByName(name);
        return ResponseEntity.ok(skill);
    }

    @PostMapping
    public ResponseEntity<SkillDto> createSkill(@Valid @RequestBody SkillCreateRequest request) {
        SkillDto skill = skillService.createSkill(request);
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillDto> updateSkill(
            @PathVariable Long id,
            @Valid @RequestBody SkillUpdateRequest request) {
        SkillDto skill = skillService.updateSkill(id, request);
        return ResponseEntity.ok(skill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getSkillCount() {
        long count = skillService.getSkillCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count-by-level/{level}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getSkillCountByLevel(@PathVariable Integer level) {
        long count = skillService.getSkillCountByLevel(level);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/count-by-level")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> getSkillCountByLevel() {
        List<Object[]> stats = skillService.getSkillCountByLevel();
        return ResponseEntity.ok(stats);
    }
}

