package com.hampcoders.glottia.platform.api.encounters.application.internal.eventhandlers;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.hampcoders.glottia.platform.api.encounters.domain.services.EncounterSeedCommandService;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedEncounterStatusesCommand;
import com.hampcoders.glottia.platform.api.encounters.domain.model.commands.SeedAttendanceStatusesCommand;

/**
 * EncountersReadyEventHandler class 
 * This class is used to handle the AplicationReadyEvent for the Encounters Bounded Context.
 */
@Service
public class EncountersReadyEventHandler {

    private final EncounterSeedCommandService encounterSeedCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EncountersReadyEventHandler.class);

    public EncountersReadyEventHandler(EncounterSeedCommandService encounterSeedCommandService) {
        this.encounterSeedCommandService = encounterSeedCommandService;
    }

    /**
     * Maneja el evento ApplicationReadyEvent para sembrar los datos de catálogo
     * de EncounterStatus y AttendanceStatus.
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting ApplicationReadyEvent handler for Encounters Bounded Context: {}", applicationName, currentTimeStamp());

        var seedEncounterStatusesCommand = new SeedEncounterStatusesCommand();
        var seedAttendanceStatusesCommand = new SeedAttendanceStatusesCommand();

        encounterSeedCommandService.handle(seedEncounterStatusesCommand);
        LOGGER.info("Encounter statuses seeded for {}.", applicationName);
        
        encounterSeedCommandService.handle(seedAttendanceStatusesCommand);
        LOGGER.info("Attendance statuses seeded for {}.", applicationName);
        
        LOGGER.info("Completed ApplicationReadyEvent handler for Encounters Bounded Context: {}", applicationName, currentTimeStamp());
    }

    private Timestamp currentTimeStamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}