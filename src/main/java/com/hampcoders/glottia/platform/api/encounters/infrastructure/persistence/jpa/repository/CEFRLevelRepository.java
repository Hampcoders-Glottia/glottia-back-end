package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.CEFRLevels;

/**
 * Repository interface for managing CEFRLevel entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom methods to find CEFRLevel by its name and check existence.
 */
@Repository
public interface CEFRLevelRepository extends JpaRepository<CEFRLevel, Long> {
    boolean existsByName(CEFRLevels name);
    Optional<CEFRLevel> findByName(CEFRLevels name);
}
