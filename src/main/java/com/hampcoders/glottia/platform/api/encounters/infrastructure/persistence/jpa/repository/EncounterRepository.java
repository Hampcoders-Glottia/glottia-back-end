package com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.EncounterStatus;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.AttendanceStatuses;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.LearnerId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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
    List<Encounter> findAllByStatusInAndScheduledAtBefore(List<EncounterStatus> statuses, LocalDateTime dateTime);

    // Búsquedas generales
    List<Encounter> findByCreatorId(LearnerId creatorId);
    List<Encounter> findByStatus(EncounterStatus status);

    // Query compleja para SearchEncountersQuery (simplificada, puede requerir Criteria API o Specification)
    @Query("SELECT e FROM Encounter e WHERE " +
        "(cast(:startOfDay as timestamp) IS NULL OR e.scheduledAt >= :startOfDay) AND " +
        "(cast(:endOfDay as timestamp) IS NULL OR e.scheduledAt <= :endOfDay) AND " +
        "(:languageId IS NULL OR e.language.id = :languageId) AND " +
        "(:cefrLevelId IS NULL OR e.level.id = :cefrLevelId)")
    Page<Encounter> findByFilters(
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay,
        @Param("languageId") Integer languageId,
        @Param("cefrLevelId") Integer cefrLevelId,
        Pageable pageable);

    /**
     * Count scheduled encounters (all non-cancelled statuses) by venue and date.
     * Groups by the date portion of scheduledAt.
     */
    @Query("""
        SELECT FUNCTION('DATE', e.scheduledAt) as encounterDate, COUNT(e) as total
        FROM Encounter e
        WHERE e.venueId.venueId = :venueId
          AND FUNCTION('DATE', e.scheduledAt) >= :startDate
          AND FUNCTION('DATE', e.scheduledAt) <= :endDate
          AND e.status.name IN (
              com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses.PUBLISHED,
              com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses.READY,
              com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses.IN_PROGRESS,
              com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses.COMPLETED
          )
        GROUP BY FUNCTION('DATE', e.scheduledAt)
        ORDER BY encounterDate
        """)
    List<Object[]> countScheduledByVenueAndDateRange(
        @Param("venueId") Long venueId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * Count completed encounters by venue and date.
     */
    @Query("""
        SELECT FUNCTION('DATE', e.scheduledAt) as encounterDate, COUNT(e) as total
        FROM Encounter e
        WHERE e.venueId.venueId = :venueId
          AND FUNCTION('DATE', e.scheduledAt) >= :startDate
          AND FUNCTION('DATE', e.scheduledAt) <= :endDate
          AND e.status.name = com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.EncounterStatuses.COMPLETED
        GROUP BY FUNCTION('DATE', e.scheduledAt)
        ORDER BY encounterDate
        """)
    List<Object[]> countCompletedByVenueAndDateRange(
        @Param("venueId") Long venueId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );


    @Query(value = "SELECT DATE(e.scheduled_at) as encounterDate, COUNT(*) as encounterCount " +
                   "FROM encounters e " +
                   "WHERE e.venue_id = :venueId " +
                   "AND e.scheduled_at BETWEEN :startDate AND :endDate " +
                   "GROUP BY DATE(e.scheduled_at)", 
           nativeQuery = true)
    List<EncounterDateCountProjection> findEncounterCountsByVenueAndDateRange(
            @Param("venueId") Long venueId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Projection interface
    interface EncounterDateCountProjection {
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate getEncounterDate();
        Long getEncounterCount();
    }
    // NUEVO: Encuentros FUTUROS donde el learner tiene asistencia activa
    @Query("SELECT DISTINCT e FROM Encounter e " +
        "JOIN e.attendances.items a " +
        "WHERE a.learnerId = :learnerId " +
        "AND (a.status.name = 'RESERVED' OR a.status.name = 'CHECKED_IN') " +
        "AND e.scheduledAt >= CURRENT_TIMESTAMP " +
        "ORDER BY e.scheduledAt ASC")
    List<Encounter> findUpcomingByLearnerId(@Param("learnerId") LearnerId learnerId);

    // NUEVO: Historial de Encuentros PASADOS (Completados, cancelados, etc.)
    @Query("SELECT DISTINCT e FROM Encounter e " +
        "JOIN e.attendances.items a " +
        "WHERE a.learnerId = :learnerId " +
        "AND e.scheduledAt < CURRENT_TIMESTAMP " +
        "ORDER BY e.scheduledAt DESC")
    List<Encounter> findHistoryByLearnerId(@Param("learnerId") LearnerId learnerId);
}