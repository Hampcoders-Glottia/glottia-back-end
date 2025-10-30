package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.AttendanceStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;

/**
 * Repository interface for managing AttendanceStatus entities.
 * Extends JpaRepository to provide CRUD operations.
 * Includes custom methods to check existence and find by name.
 */
@Repository
public interface AttendanceStatusRepository extends JpaRepository<AttendanceStatus, Long> {
    boolean existsByName(AttendanceStatuses name);
    Optional<AttendanceStatus> findByName(AttendanceStatuses name);
}
