package com.smartportfolio.service;

import com.smartportfolio.dto.*;
import com.smartportfolio.exception.ResourceNotFoundException;
import com.smartportfolio.model.Blog;
import com.smartportfolio.repository.BlogRepository;
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
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<BlogDto> getAllPublishedBlogs() {
        log.info("Tüm yayınlanmış bloglar getiriliyor");
        List<Blog> blogs = blogRepository.findByIsPublishedTrueOrderByCreatedAtDesc();
        return blogs.stream()
                .map(blog -> modelMapper.map(blog, BlogDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BlogDto> getAllBlogs() {
        log.info("Tüm bloglar getiriliyor (admin)");
        List<Blog> blogs = blogRepository.findAllByOrderByCreatedAtDesc();
        return blogs.stream()
                .map(blog -> modelMapper.map(blog, BlogDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<BlogDto> getAllPublishedBlogs(Pageable pageable) {
        log.info("Tüm yayınlanmış bloglar sayfalı olarak getiriliyor");
        Page<Blog> blogs = blogRepository.findByIsPublishedTrueOrderByCreatedAtDesc(pageable);
        return blogs.map(blog -> modelMapper.map(blog, BlogDto.class));
    }

    @Transactional(readOnly = true)
    public Page<BlogDto> searchBlogs(String keyword, Pageable pageable) {
        log.info("Bloglar aranıyor - keyword: {}", keyword);
        Page<Blog> blogs = blogRepository.findByKeywordAndIsPublishedTrue(keyword, pageable);
        return blogs.map(blog -> modelMapper.map(blog, BlogDto.class));
    }

    @Transactional(readOnly = true)
    public List<BlogDto> getTopBlogsByViews(Pageable pageable) {
        log.info("En çok okunan bloglar getiriliyor");
        List<Blog> blogs = blogRepository.findTopByViewCountOrderByViewCountDesc(pageable);
        return blogs.stream()
                .map(blog -> modelMapper.map(blog, BlogDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BlogDto getBlogById(Long id) {
        log.info("Blog getiriliyor - ID: {}", id);
        Blog blog = blogRepository.findByIdAndIsPublishedTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id));
        
        // Görüntülenme sayısını artır
        blog.setViewCount(blog.getViewCount() + 1);
        blogRepository.save(blog);
        
        return modelMapper.map(blog, BlogDto.class);
    }

    @Transactional
    public BlogDto createBlog(BlogCreateRequest request) {
        log.info("Yeni blog oluşturuluyor: {}", request.getTitle());
        
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .imageUrl(request.getImageUrl())
                .isPublished(request.getIsPublished() != null ? request.getIsPublished() : false)
                .viewCount(0L)
                .build();

        Blog savedBlog = blogRepository.save(blog);
        log.info("Blog başarıyla oluşturuldu - ID: {}", savedBlog.getId());
        
        return modelMapper.map(savedBlog, BlogDto.class);
    }

    @Transactional
    public BlogDto updateBlog(Long id, BlogUpdateRequest request) {
        log.info("Blog güncelleniyor - ID: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id));

        if (request.getTitle() != null) blog.setTitle(request.getTitle());
        if (request.getContent() != null) blog.setContent(request.getContent());
        if (request.getSummary() != null) blog.setSummary(request.getSummary());
        if (request.getImageUrl() != null) blog.setImageUrl(request.getImageUrl());
        if (request.getIsPublished() != null) blog.setIsPublished(request.getIsPublished());

        Blog updatedBlog = blogRepository.save(blog);
        log.info("Blog başarıyla güncellendi - ID: {}", id);
        
        return modelMapper.map(updatedBlog, BlogDto.class);
    }

    @Transactional
    public void deleteBlog(Long id) {
        log.info("Blog siliniyor - ID: {}", id);
        
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id));
        
        blogRepository.delete(blog);
        log.info("Blog başarıyla silindi - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long getPublishedBlogCount() {
        return blogRepository.countByIsPublishedTrue();
    }

    @Transactional(readOnly = true)
    public Long getTotalViewCount() {
        return blogRepository.getTotalViewCount();
    }
}

