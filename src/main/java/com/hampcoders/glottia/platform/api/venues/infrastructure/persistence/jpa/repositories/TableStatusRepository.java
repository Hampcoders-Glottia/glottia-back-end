package com.hampcoders.glottia.platform.api.venues.infrastructure.persistence.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.venues.domain.model.entities.TableStatus;
import com.hampcoders.glottia.platform.api.venues.domain.model.valueobjects.TableStatuses;

@Repository
public interface TableStatusRepository extends JpaRepository<TableStatus, Long> {
    boolean existsByName(TableStatuses name);
    Optional<TableStatus> findByName(TableStatuses name);
}