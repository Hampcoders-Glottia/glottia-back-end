package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses;

@Repository
public interface EncounterStatusRepository extends JpaRepository<EncounterStatus, Long> {
    boolean existsByName(EncounterStatuses name);
    Optional<EncounterStatus> findByName(EncounterStatuses name);

}
