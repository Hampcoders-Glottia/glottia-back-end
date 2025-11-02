package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for managing Encounter entities.
 * Provides methods for performing CRUD operations and specific queries.
 * Includes custom queries to support business rules and search functionality.
 */
@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

    // Para la regla de negocio: "Un Learner no puede estar en dos encounters simultáneos"
    @Query("SELECT e FROM Encounter e JOIN e.attendances.items a WHERE a.learnerId = :learnerId AND e.status.name IN ('PUBLISHED', 'READY', 'IN_PROGRESS') AND e.scheduledAt BETWEEN :start AND :end")
    List<Encounter> findConflictingEncounters(
            @Param("learnerId") LearnerId learnerId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // Para el Scheduler: Cancelación automática
    List<Encounter> findAllByStatusInAndScheduledAtBefore(List<AttendanceStatuses> statuses, LocalDateTime dateTime);

    // Búsquedas generales
    List<Encounter> findByCreatorId(LearnerId creatorId);
    List<Encounter> findByStatus(EncounterStatus status);

    // Query compleja para SearchEncountersQuery (simplificada, puede requerir Criteria API o Specification)
    @Query("SELECT e FROM Encounter e WHERE e.status.name IN ('PUBLISHED', 'READY') " +
       "AND (e.language.id = :languageId) " +
       "AND (e.level.id = :cefrLevelId) " +
       "AND (cast(e.scheduledAt as date) = :date)")
    List<Encounter> findByFilters(
            @Param("languageId") Long languageId,
            @Param("cefrLevelId") Long cefrLevelId,
            @Param("date") LocalDateTime date);
}