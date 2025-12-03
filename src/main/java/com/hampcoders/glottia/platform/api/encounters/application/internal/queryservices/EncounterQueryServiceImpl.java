package com.hampcoders.glottia.platform.api.encounters.application.internal.queryservices;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.*;
import com.hampcoders.glottia.platform.api.encounters.domain.model.valueobjects.DailyEncounterStat;
import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterQueryService;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.AttendanceRepository;
import com.hampcoders.glottia.platform.api.encounters.infrastructure.persistence.jpa.repository.EncounterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return encounterRepository.findUpcomingByLearnerId(query.learnerId());
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

    @Override
    public List<DailyEncounterStat> handle(GetEncounterStatsByVenueQuery query) {
        // Get scheduled counts from repository
        List<Object[]> scheduledResults = encounterRepository.countScheduledByVenueAndDateRange(
            query.venueId(), query.startDate(), query.endDate()
        );
        
        // Get completed counts from repository
        List<Object[]> completedResults = encounterRepository.countCompletedByVenueAndDateRange(
            query.venueId(), query.startDate(), query.endDate()
        );
        
        // Merge results into a map: date -> [scheduled, completed]
        Map<LocalDate, long[]> statsMap = new HashMap<>();
        
        // Initialize all dates in range with zeros
        LocalDate current = query.startDate();
        while (!current.isAfter(query.endDate())) {
            statsMap.put(current, new long[]{0L, 0L});
            current = current.plusDays(1);
        }
        
        // Populate scheduled counts - FIX: Convert java.sql.Date to LocalDate
        for (Object[] row : scheduledResults) {
            LocalDate date = convertToLocalDate(row[0]); // <--- FIX HERE
            long count = ((Number) row[1]).longValue();
            if (statsMap.containsKey(date)) {
                statsMap.get(date)[0] = count;
            }
        }
        
        // Populate completed counts - FIX: Convert java.sql.Date to LocalDate
        for (Object[] row : completedResults) {
            LocalDate date = convertToLocalDate(row[0]); // <--- FIX HERE
            long count = ((Number) row[1]).longValue();
            if (statsMap.containsKey(date)) {
                statsMap.get(date)[1] = count;
            }
        }
        
        // Convert to List<DailyEncounterStat> (domain value objects)
        List<DailyEncounterStat> result = new ArrayList<>();
        
        statsMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> result.add(new DailyEncounterStat(
                entry.getKey(),
                entry.getValue()[0],
                entry.getValue()[1]
            )));
        
        return result;
    }

    @Override
    public List<Encounter> handle(GetEncountersByLearnerIdQuery query) {
        return encounterRepository.findAll().stream()
            .filter(encounter -> encounter.getAttendances()
                .hasLearner(query.learnerId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Encounter> handle(SearchEncountersSimpleQuery query) {
        var allEncounters = encounterRepository.findAll();
        
        return allEncounters.stream()
            .filter(encounter -> {
                boolean matches = true;
                
                if (query.languageId().isPresent()) {
                    matches = matches && encounter.getLanguage().getId()
                        .equals(query.languageId().get());
                }
                
                if (query.cefrLevelId().isPresent()) {
                    matches = matches && encounter.getLevel().getId()
                        .equals(query.cefrLevelId().get());
                }
                
                if (query.topic().isPresent()) {
                    matches = matches && encounter.getTopic()
                        .toLowerCase().contains(query.topic().get().toLowerCase());
                }
                
                return matches;
            })
            .skip((long) query.page() * query.size())
            .limit(query.size())
            .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDate(Object dateObject) {
        if (dateObject == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        
        if (dateObject instanceof LocalDate) {
            return (LocalDate) dateObject;
        }
        
        if (dateObject instanceof java.sql.Date) {
            return ((java.sql.Date) dateObject).toLocalDate();
        }
        
        throw new IllegalArgumentException(
            "Unsupported date type: " + dateObject.getClass().getName()
        );
    }
    // Nueva implementación para historial (Asegúrate de agregar el método a la interfaz EncounterQueryService primero)
    public List<Encounter> handle(GetLearnerEncounterHistoryQuery query) {
        return encounterRepository.findHistoryByLearnerId(query.learnerId());
    }
}
