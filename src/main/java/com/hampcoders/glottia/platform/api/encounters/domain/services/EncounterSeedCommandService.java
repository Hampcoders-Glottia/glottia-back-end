package com.hampcoders.glottia.platform.api.encounters.domain.services;

import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedAttendanceStatusesCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedEncounterStatusesCommand;

public interface EncounterSeedCommandService {
    /**
     * Maneja el comando para sembrar los estados de Encounter.
     * @param command El comando {@link SeedEncounterStatusesCommand}
     */
    void handle(SeedEncounterStatusesCommand command);

    /**
     * Maneja el comando para sembrar los estados de Attendance.
     * @param command El comando {@link SeedAttendanceStatusesCommand}
     */
    void handle(SeedAttendanceStatusesCommand command);
}