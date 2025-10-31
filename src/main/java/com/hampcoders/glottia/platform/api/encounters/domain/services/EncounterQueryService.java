package com.hampcoders.glottia.platform.api.encounters.domain.services;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.entities.Attendance;
import com.hampcoders.glottia.platform.api.encounters.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface EncounterQueryService {
    Optional<Encounter> handle(GetEncounterByIdQuery query);
    List<Encounter> handle(SearchEncountersQuery query); // Considera paginación aquí
    List<Encounter> handle(GetAttendancesByLearnerIdAndAttendanceStatusIdQuery query);
    List<Encounter> handle(GetUpcomingEncountersForLearnerQuery query);
    List<Encounter> handle(GetEncountersRequiringAutoCheckQuery query); // Para el Scheduler
    List<Attendance> handle(GetAttendancesByEncounterQuery query);
    List<Attendance> handle(GetEncountersByCreatorIdAndEncounterStatusIdQuery query);
    boolean handle(HasConflictingEncounterQuery query); // Devuelve boolean
    Optional<Object> handle(GetAttendanceStatsQuery query); // Devuelve estadísticas
}