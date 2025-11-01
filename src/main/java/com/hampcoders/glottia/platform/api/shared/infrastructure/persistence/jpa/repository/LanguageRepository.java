package com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.Language;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for Language entity
 * Repository for Language entity operations in the Profiles bounded context
 *
 * @author Hampcoders
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    
    /**
     * Find language by language enum
     * @param name the language enum to search for
     * @return Optional<Language> containing the language if found
     */
    Optional<Language> findByName(Languages name);
    
    /**
     * Check if language exists by language enum
     * @param name the language enum to check
     * @return true if language exists, false otherwise
     */
    boolean existsByName(Languages name);
}