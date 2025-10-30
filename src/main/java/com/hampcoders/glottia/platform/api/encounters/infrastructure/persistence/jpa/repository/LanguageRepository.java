package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.Languages;

/**
 * Repository interface for managing Language entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom methods to check existence and find by name.
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    boolean existsByName(Languages name);
    Optional<Language> findByName(Languages name);
}
