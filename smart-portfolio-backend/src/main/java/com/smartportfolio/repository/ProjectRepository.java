package com.smartportfolio.repository;

import com.smartportfolio.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByIsActiveTrueOrderByCreatedAtDesc();
    
    List<Project> findByIsFeaturedTrueAndIsActiveTrueOrderByCreatedAtDesc();
    
    Page<Project> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.isActive = true AND " +
           "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Project> findByKeywordAndIsActiveTrue(@Param("keyword") String keyword, Pageable pageable);
    
    Optional<Project> findByIdAndIsActiveTrue(Long id);
    
    long countByIsActiveTrue();
    
    long countByIsFeaturedTrueAndIsActiveTrue();
}

