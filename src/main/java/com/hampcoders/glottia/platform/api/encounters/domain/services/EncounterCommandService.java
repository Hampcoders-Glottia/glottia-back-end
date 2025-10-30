package com.hampcoders.glottia.platform.api.encounters.domain.services;

import com.hampcoders.glottia.platform.api.encounters.domain.model.aggregates.Encounter;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.*;

import java.util.Optional;

public interface EncounterCommandService {
    Optional<Encounter> handle(CreateEncounterCommand command); // Devuelve Optional<Encounter> para obtener el ID y estado inicial
    void handle(AssignTableToEncounterCommand command); // Comando interno o llamado por event handler
    Optional<Encounter> handle(PublishEncounterCommand command);
    Optional<Encounter> handle(JoinEncounterCommand command); // Podría devolver Attendance o Encounter actualizado
    Optional<Encounter> handle(CheckInEncounterCommand command);
    Optional<Encounter> handle(StartEncounterCommand command);
    Optional<Encounter> handle(CompleteEncounterCommand command);
    Optional<Encounter> handle(CancelEncounterCommand command);
    Optional<Encounter> handle(CancelAttendanceCommand command); // Podría devolver Attendance o Encounter actualizado

    // Comando para el Scheduler
    void handle(AutoCancelEncountersCommand command);
}


