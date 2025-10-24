package com.smartportfolio.repository;

import com.smartportfolio.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    
    List<Language> findByIsActiveTrueOrderByNameAsc();
    
    Optional<Language> findByCodeIgnoreCaseAndIsActiveTrue(String code);
    
    Optional<Language> findByIdAndIsActiveTrue(Long id);
    
    Optional<Language> findByIsDefaultTrueAndIsActiveTrue();
    
    @Query("SELECT l FROM Language l WHERE l.isActive = true AND " +
           "(LOWER(l.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY l.name ASC")
    List<Language> findByKeywordAndIsActiveTrue(@Param("keyword") String keyword);
    
    boolean existsByCodeIgnoreCaseAndIsActiveTrue(String code);
    
    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);
    
    long countByIsActiveTrue();
    
    @Query("SELECT COUNT(l) FROM Language l WHERE l.isDefault = true AND l.isActive = true")
    long countByIsDefaultTrueAndIsActiveTrue();
}

