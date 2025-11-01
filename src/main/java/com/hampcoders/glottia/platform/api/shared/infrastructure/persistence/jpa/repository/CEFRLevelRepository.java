package com.hampcoders.glottia.platform.api.shared.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.shared.domain.model.entities.CEFRLevel;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.CEFRLevels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CEFRLevelRepository extends JpaRepository<CEFRLevel, Long> {
    Optional<CEFRLevel> findByName(CEFRLevels level);
    boolean existsByName(CEFRLevels level);
}
