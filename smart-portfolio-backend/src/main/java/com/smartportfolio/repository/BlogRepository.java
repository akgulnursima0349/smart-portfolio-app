package com.smartportfolio.repository;

import com.smartportfolio.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    List<Blog> findByIsPublishedTrueOrderByCreatedAtDesc();
    
    List<Blog> findAllByOrderByCreatedAtDesc();
    
    Page<Blog> findByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT b FROM Blog b WHERE b.isPublished = true AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.summary) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Blog> findByKeywordAndIsPublishedTrue(@Param("keyword") String keyword, Pageable pageable);
    
    Optional<Blog> findByIdAndIsPublishedTrue(Long id);
    
    @Query("SELECT b FROM Blog b WHERE b.isPublished = true ORDER BY b.viewCount DESC")
    List<Blog> findTopByViewCountOrderByViewCountDesc(Pageable pageable);
    
    long countByIsPublishedTrue();
    
    @Query("SELECT SUM(b.viewCount) FROM Blog b WHERE b.isPublished = true")
    Long getTotalViewCount();
}

