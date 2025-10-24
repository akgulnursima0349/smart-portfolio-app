package com.smartportfolio.repository;

import com.smartportfolio.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    
    List<Skill> findByIsActiveTrueOrderBySortOrderAscNameAsc();
    
    List<Skill> findByLevelAndIsActiveTrueOrderBySortOrderAscNameAsc(Integer level);
    
    @Query("SELECT s FROM Skill s WHERE s.isActive = true AND " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY s.sortOrder ASC, s.name ASC")
    List<Skill> findByKeywordAndIsActiveTrue(@Param("keyword") String keyword);
    
    Optional<Skill> findByIdAndIsActiveTrue(Long id);
    
    Optional<Skill> findByNameIgnoreCaseAndIsActiveTrue(String name);
    
    boolean existsByNameIgnoreCaseAndIsActiveTrue(String name);
    
    long countByIsActiveTrue();
    
    long countByLevelAndIsActiveTrue(Integer level);
    
    @Query("SELECT s.level, COUNT(s) FROM Skill s WHERE s.isActive = true GROUP BY s.level")
    List<Object[]> getSkillCountByLevel();
}

