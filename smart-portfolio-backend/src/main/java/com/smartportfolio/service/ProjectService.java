package com.smartportfolio.service;

import com.smartportfolio.dto.*;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.Project;
import com.smartportfolio.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        log.info("Tüm aktif projeler getiriliyor");
        List<Project> projects = projectRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> getAllProjects(Pageable pageable) {
        log.info("Tüm aktif projeler sayfalı olarak getiriliyor");
        Page<Project> projects = projectRepository.findByIsActiveTrueOrderByCreatedAtDesc(pageable);
        return projects.map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> getFeaturedProjects() {
        log.info("Öne çıkan projeler getiriliyor");
        List<Project> projects = projectRepository.findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedAtDesc();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> searchProjects(String keyword, Pageable pageable) {
        log.info("Projeler aranıyor - keyword: {}", keyword);
        Page<Project> projects = projectRepository.findByKeywordAndIsActiveTrue(keyword, pageable);
        return projects.map(project -> modelMapper.map(project, ProjectDto.class));
    }

    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        log.info("Proje getiriliyor - ID: {}", id);
        Project project = projectRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return modelMapper.map(project, ProjectDto.class);
    }

    @Transactional
    public ProjectDto createProject(ProjectCreateRequest request) {
        log.info("Yeni proje oluşturuluyor: {}", request.getTitle());
        
        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .githubUrl(request.getGithubUrl())
                .demoUrl(request.getDemoUrl())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isActive(true)
                .build();

        Project savedProject = projectRepository.save(project);
        log.info("Proje başarıyla oluşturuldu - ID: {}", savedProject.getId());
        
        return modelMapper.map(savedProject, ProjectDto.class);
    }

    @Transactional
    public ProjectDto updateProject(Long id, ProjectUpdateRequest request) {
        log.info("Proje güncelleniyor - ID: {}", id);
        
        Project project = projectRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        if (request.getTitle() != null) project.setTitle(request.getTitle());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getImageUrl() != null) project.setImageUrl(request.getImageUrl());
        if (request.getGithubUrl() != null) project.setGithubUrl(request.getGithubUrl());
        if (request.getDemoUrl() != null) project.setDemoUrl(request.getDemoUrl());
        if (request.getIsActive() != null) project.setIsActive(request.getIsActive());
        if (request.getIsFeatured() != null) project.setIsFeatured(request.getIsFeatured());

        Project updatedProject = projectRepository.save(project);
        log.info("Proje başarıyla güncellendi - ID: {}", id);
        
        return modelMapper.map(updatedProject, ProjectDto.class);
    }

    @Transactional
    public void deleteProject(Long id) {
        log.info("Proje siliniyor - ID: {}", id);
        
        Project project = projectRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        
        project.setIsActive(false);
        projectRepository.save(project);
        
        log.info("Proje başarıyla silindi - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long getProjectCount() {
        return projectRepository.countByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public long getFeaturedProjectCount() {
        return projectRepository.countByIsFeaturedTrueAndIsActiveTrue();
    }
}

