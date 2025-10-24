package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Proje yönetimi işlemleri")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Tüm projeleri listele", description = "Aktif tüm projeleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeler başarıyla getirildi",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Sayfalı proje listesi", description = "Projeleri sayfalı olarak getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeler başarıyla getirildi",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<ProjectDto>> getAllProjects(Pageable pageable) {
        Page<ProjectDto> projects = projectService.getAllProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Öne çıkan projeler", description = "Öne çıkan projeleri getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Öne çıkan projeler getirildi",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class)))
    })
    @GetMapping("/featured")
    public ResponseEntity<List<ProjectDto>> getFeaturedProjects() {
        List<ProjectDto> projects = projectService.getFeaturedProjects();
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Proje arama", description = "Anahtar kelime ile proje arar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arama sonuçları getirildi",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProjectDto>> searchProjects(
            @Parameter(description = "Arama anahtar kelimesi", required = true)
            @RequestParam String keyword,
            Pageable pageable) {
        Page<ProjectDto> projects = projectService.searchProjects(keyword, pageable);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Proje detayı", description = "ID ile proje detayını getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proje detayı getirildi",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "404", description = "Proje bulunamadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @Parameter(description = "Proje ID'si", required = true)
            @PathVariable Long id) {
        ProjectDto project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Proje oluştur", description = "Yeni proje oluşturur (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proje başarıyla oluşturuldu",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Parameter(description = "Proje oluşturma bilgileri", required = true)
            @Valid @RequestBody ProjectCreateRequest request) {
        ProjectDto project = projectService.createProject(request);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @Operation(summary = "Proje güncelle", description = "Mevcut projeyi günceller (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proje başarıyla güncellendi",
                    content = @Content(schema = @Schema(implementation = ProjectDto.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş verisi"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim"),
            @ApiResponse(responseCode = "404", description = "Proje bulunamadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "Proje ID'si", required = true)
            @PathVariable Long id,
            @Parameter(description = "Proje güncelleme bilgileri", required = true)
            @Valid @RequestBody ProjectUpdateRequest request) {
        ProjectDto project = projectService.updateProject(id, request);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Proje sil", description = "Projeyi siler (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proje başarıyla silindi"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim"),
            @ApiResponse(responseCode = "404", description = "Proje bulunamadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Proje ID'si", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Proje sayısı", description = "Toplam proje sayısını getirir (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proje sayısı getirildi"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @GetMapping("/stats/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getProjectCount() {
        long count = projectService.getProjectCount();
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Öne çıkan proje sayısı", description = "Öne çıkan proje sayısını getirir (ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Öne çıkan proje sayısı getirildi"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @GetMapping("/stats/featured-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getFeaturedProjectCount() {
        long count = projectService.getFeaturedProjectCount();
        return ResponseEntity.ok(count);
    }
}