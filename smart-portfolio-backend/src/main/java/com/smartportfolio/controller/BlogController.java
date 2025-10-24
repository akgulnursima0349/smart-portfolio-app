package com.smartportfolio.controller;

import com.smartportfolio.dto.*;
import com.smartportfolio.service.BlogService;
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
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogDto>> getAllPublishedBlogs() {
        List<BlogDto> blogs = blogService.getAllPublishedBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<BlogDto>> getAllBlogs() {
        List<BlogDto> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<BlogDto>> getAllPublishedBlogs(Pageable pageable) {
        Page<BlogDto> blogs = blogService.getAllPublishedBlogs(pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BlogDto>> searchBlogs(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<BlogDto> blogs = blogService.searchBlogs(keyword, pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/top")
    public ResponseEntity<List<BlogDto>> getTopBlogsByViews(Pageable pageable) {
        List<BlogDto> blogs = blogService.getTopBlogsByViews(pageable);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDto> getBlogById(@PathVariable Long id) {
        BlogDto blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }

    @PostMapping
    public ResponseEntity<BlogDto> createBlog(@Valid @RequestBody BlogCreateRequest request) {
        BlogDto blog = blogService.createBlog(request);
        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDto> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogUpdateRequest request) {
        BlogDto blog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(blog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats/published-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getPublishedBlogCount() {
        long count = blogService.getPublishedBlogCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/total-views")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getTotalViewCount() {
        Long totalViews = blogService.getTotalViewCount();
        return ResponseEntity.ok(totalViews);
    }
}

