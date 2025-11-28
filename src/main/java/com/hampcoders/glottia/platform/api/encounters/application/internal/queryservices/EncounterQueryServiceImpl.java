package com.hampcoders.glottia.platform.api.encounters.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.AttendanceRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/** 
 * Implements the EncounterQueryService to handle queries related to encounters.
 * This service manages retrieval of encounter data based on various criteria,
 * interacting with necessary repositories to fetch and return the requested information.
 */
@Service
public class EncounterQueryServiceImpl implements EncounterQueryService {

    private final EncounterRepository encounterRepository;
    private final AttendanceRepository attendanceRepository;

    public EncounterQueryServiceImpl(EncounterRepository encounterRepository, AttendanceRepository attendanceRepository) {
        this.encounterRepository = encounterRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Optional<Encounter> handle(GetEncounterByIdQuery query) {
        return encounterRepository.findById(query.encounterId());
    }

    @Override
    public List<Encounter> handle(SearchEncountersQuery query) {
        // CORRECCIÓN: Convertir LocalDate a Rango de LocalDateTime
        LocalDateTime startOfDay = null;
        LocalDateTime endOfDay = null;

        if (query.date() != null) {
            startOfDay = query.date().atStartOfDay(); // 00:00:00
            endOfDay = query.date().atTime(java.time.LocalTime.MAX); // 23:59:59.999
        }

        // Llamamos al repositorio con los parámetros corregidos
        var encounterPage = encounterRepository.findByFilters(
            startOfDay,
            endOfDay,
            query.languageId().intValue(),
            query.cefrlevelId().intValue(),
            PageRequest.of(query.page(), query.size())
        );

        return encounterPage.getContent();
    }

    /* @Override
    public List<Encounter> handle(GetAttendancesByLearnerIdAndAttendanceStatusIdQuery query) {
        AttendanceStatus status = attendanceStatusRepository.findById(query.attendanceStatusId())
                .orElseThrow(() -> new IllegalArgumentException("Attendance Status not found"));
        return encounterRepository.findAllByStatusInAndScheduledAtBefore(List.of(status), query.learnerId());
    } */


    @Override
    public List<Encounter> handle(GetUpcomingEncountersForLearnerQuery query) {
        // Esta query es compleja y requiere una implementación personalizada en el repositorio
        // (Ver `findConflictingEncounters` como ejemplo de query con JOIN)
        // Por simplicidad, simulamos:
        return List.of(); // Debería implementarse en EncounterRepository
    }

    /*     @Override
    public List<Encounter> handle(GetEncountersRequiringAutoCheckQuery query) {
        EncounterStatus readyStatus = encounterStatusRepository.findByName(EncounterStatuses.READY).orElse(null);
        EncounterStatus publishedStatus = encounterStatusRepository.findByName(EncounterStatuses.PUBLISHED).orElse(null);
        
        if (readyStatus == null || publishedStatus == null) return List.of();
        
        return encounterRepository.findAllByStatusInAndScheduledAtBefore(
                List.of(readyStatus, publishedStatus),
                query.thresholdTime()
        );
    } */

    @Override
    public List<Attendance> handle(GetAttendancesByEncounterQuery query) {
        return attendanceRepository.findByEncounterId(query.encounterId());
    }

    @Override
    public List<Attendance> handle(GetEncountersByCreatorIdAndEncounterStatusIdQuery query) {
        return attendanceRepository.findByEncounterId(query.encounterStatusId());
    }

    @Override
    public boolean handle(HasConflictingEncounterQuery query) {
        LocalDateTime start = query.scheduledAt().minusHours(2);
        LocalDateTime end = query.scheduledAt().plusHours(2);
        return !encounterRepository.findConflictingEncounters(query.learnerId(), start, end).isEmpty();
    }

    @Override
    public Optional<Object> handle(GetAttendanceStatsQuery query) {
        // Esto normalmente devolvería un DTO/Projection con conteos
        // (COUNT... GROUP BY status)
        return Optional.empty(); // Implementación pendiente
    }
}
